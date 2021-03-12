package ua.smartsub.smartsub.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ua.smartsub.smartsub.model.DTO.*;
import ua.smartsub.smartsub.model.entity.EmailVerificationToken;
import ua.smartsub.smartsub.model.entity.RefreshToken;

import ua.smartsub.smartsub.event.OnRegenerateEmailVerificationEvent;
import ua.smartsub.smartsub.event.OnUserAccountChangeEvent;
import ua.smartsub.smartsub.event.OnUserRegistrationCompleteEvent;
import ua.smartsub.smartsub.exception.*;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.security.CustomUserDetails;
import ua.smartsub.smartsub.security.jwt.JwtProvider;
import ua.smartsub.smartsub.services.IAuthService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private IAuthService authService;
    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private  ApplicationEventPublisher applicationEventPublisher;


    @GetMapping("/checkEmailInUse")
    public ResponseEntity checkEmailInUse(@RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(emailExists.toString(), true));
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity registerUser(@RequestBody @Valid RegisterDTO registerDTO) {
        return authService.registerUser(registerDTO)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    log.info("Registered User returned [API[: " + user);
                    return ResponseEntity.ok(new ApiResponse("User registered successfully. Check your email for verification", true));
                })
                .orElseThrow(() -> new UserRegistrationException(registerDTO.getEmail(), "Missing user object in database"));

    }

    @ApiOperation(value = "Checks if the given username is in use")
    @GetMapping("/checkUsernameInUse")
    public ResponseEntity checkUsernameInUse(@ApiParam(value = "Username to check against") @RequestParam(
            "username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(usernameExists.toString(), true));
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO) {
        Authentication authentication = authService.authenticateUser(loginDTO).orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginDTO + "]"));
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Logged in User returned [API]: " + customUserDetails.getUsername());
        return authService.createAndPersistRefreshTokenForDevice(authentication, loginDTO)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    //generate new access token
                    String jwtToken = authService.generateToken(customUserDetails);
                    //then return token and access token
                    return ResponseEntity.ok(new JwtAuthenticationDTO(jwtToken, refreshToken,"Bearer " , tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginDTO + "]"));
    }


    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
    public ResponseEntity confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {
        return authService.confirmEmailRegistration(token)
                .map(user -> ResponseEntity.ok(new ApiResponse("User verified successfully", true)))
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token, "Failed to confirm. Please generate a new email verification request"));
    }

    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
            "assume that the user would always click on the last re-verification email and " +
            "any attempts at generating new token from past (possibly archived/deleted)" +
            "tokens should fail and report an exception. ")
    public ResponseEntity resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user email after registration") @RequestParam("token") String existingToken) {

        EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token"));

        return Optional.ofNullable(newEmailToken.getUser())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent((User) registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true));
                })
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No user associated with this request. Re-verification denied"));
    }


    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
            "email")
    public ResponseEntity resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetDTO passwordResetRequest) {

        return authService.resetPassword(passwordResetRequest)
                .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
            "updated response tokens")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {

        return authService.refreshJwtToken(refreshTokenDTO)
                .map(updatedToken -> {
                    String refreshToken = refreshTokenDTO.getRefreshToken();
                    log.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationDTO(updatedToken, refreshToken, "Bearer ", tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshTokenDTO.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));
    }

}
