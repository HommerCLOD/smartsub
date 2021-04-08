package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ua.smartsub.smartsub.event.OnRegenerateEmailVerificationEvent;
import ua.smartsub.smartsub.event.OnUserAccountChangeEvent;
import ua.smartsub.smartsub.event.OnUserRegistrationCompleteEvent;
import ua.smartsub.smartsub.exception.*;
import ua.smartsub.smartsub.model.DTO.*;
import ua.smartsub.smartsub.dao.RoleDao;
import ua.smartsub.smartsub.model.entity.*;
import ua.smartsub.smartsub.security.CustomUserDetails;
import ua.smartsub.smartsub.security.jwt.JwtProvider;
import ua.smartsub.smartsub.services.*;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService {


    @Autowired
    private IUserService userService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserDeviceService userDeviceService;
    @Autowired
    private IRefreshTokenService refreshTokenService;
    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;
    @Autowired
    private IPasswordResetTokenService passwordResetTokenService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public ResponseEntity registerUser(RegisterDTO registerDTO) {
        if (usernameAlreadyExists(registerDTO.getUsername())) {
            log.error("Username already exists: " + registerDTO.getUsername());
            throw new UniqueUserException("Юзер з таким логіном вже існує");
        } else if (emailAlreadyExists(registerDTO.getEmail())) {
            log.error("Email already exists: " + registerDTO.getEmail());
            throw new UniqueUserException("Юзер з таким е-маіл вже існує");
        }
        log.info("Trying to register new user [" + registerDTO.getUsername() + "]");
        Role userRole = roleDao.findByName("ROLE_USER");
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmailVerified(false);

        return Optional.ofNullable(userService.save(user))
                .map(us -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(us, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    log.info("Registered User returned [API[: " + us);
                    return ResponseEntity.ok(new ApiResponse("User registered successfully. Check your email for verification", true));
                })
                .orElseThrow(() -> new UserRegistrationException(registerDTO.getEmail(), "Missing user object in database"));

    }

    public User findByLoginAndPassword(String username, String password) {
        User user = userService.findByName(username).orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Optional<Authentication> authenticateUser(LoginDTO loginDTO) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword())));
    }

    public ResponseEntity createAndPersistRefreshTokenForDevice(Authentication authentication, LoginDTO loginDTO, CustomUserDetails customUserDetails) {
        User currentUser = (User) authentication.getPrincipal();
        userDeviceService.findByUserId(currentUser.getId())
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);
        UserDevice userDevice = userDeviceService.createUserDevice(loginDTO.getDeviceInfo());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setUser(currentUser);
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken).map(RefreshToken::getToken)
                .map(refresh -> {
                    //generate new access token
                    String jwtToken = this.generateToken(customUserDetails);
                    //then return token and access token
                    return ResponseEntity.ok(new JwtAuthenticationDTO(jwtToken, refresh,"Bearer " , tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginDTO + "]"));
    }

    public Optional<User> confirmEmailRegistration(String emailToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(emailToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification", emailToken));

        User registeredUser = emailVerificationToken.getUser();
        if (registeredUser.getEmailVerified()) {
            log.info("User [" + emailToken + "] already registered.");
            return Optional.of(registeredUser);
        }

        emailVerificationTokenService.verifyExpiration(emailVerificationToken);
        emailVerificationToken.setConfirmedStatus();
        emailVerificationTokenService.save(emailVerificationToken);

        registeredUser.setEmailVerified(true);
        userService.save(registeredUser);
        return Optional.of(registeredUser);
    }

    public ResponseEntity recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Existing email verification", existingToken));

        if (emailVerificationToken.getUser().getEmailVerified()) {
            throw new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token");
        }
        EmailVerificationToken newEmailToken = emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken);

                return Optional.ofNullable(newEmailToken.getUser())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent((User) registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true));
                })
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No user associated with this request. Re-verification denied"));

    }

    public ResponseEntity resetPassword(PasswordResetDTO passwordResetDTO) {
        String token = passwordResetDTO.getToken();
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset Token", "Token Id", token));

        passwordResetTokenService.verifyExpiration(passwordResetToken);
        final String encodedPassword = passwordEncoder.encode(passwordResetDTO.getPassword());

        return Optional.of(passwordResetToken)
                .map(PasswordResetToken::getUser)
                .map(user -> {
                    user.setPassword(encodedPassword);
                    userService.save(user);
                    return user;
                }) .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetDTO.getToken(), "Error in resetting password"));

    }

    public ResponseEntity refreshJwtToken(RefreshTokenDTO refreshTokenDTO) {
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();

        return Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userDeviceService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserDevice)
                .map(UserDevice::getUser)
                .map(User::getUsername).map(this::generateTokenFromUserName))
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Missing refresh token in database.Please login again"))
                .map(updatedToken -> {
                    String refreshToken = refreshTokenDTO.getRefreshToken();
                    log.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationDTO(updatedToken, refreshToken, "Bearer ", tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshTokenDTO.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));



    }

    public Optional<User> updatePassword(CustomUserDetails customUserDetails,
                                         UpdatePasswordDTO updatePasswordRequest) {
        String email = customUserDetails.getEmail();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new UpdatePasswordException(email, "No matching user found"));

        if (!currentPasswordMatches(currentUser, updatePasswordRequest.getOldPassword())) {
            log.info("Current password is invalid for [" + currentUser.getPassword() + "]");
            throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
        }
        String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        currentUser.setPassword(newPassword);
        userService.save(currentUser);
        return Optional.of(currentUser);
    }

    private Boolean currentPasswordMatches(User currentUser, String password) {
        return passwordEncoder.matches(password, currentUser.getPassword());
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        return tokenProvider.generateToken(customUserDetails);
    }

    private String generateTokenFromUserName(String username) {
        return tokenProvider.generateAccessToken(username);
    }

    public Optional<User> findByLogin(String login) {
        return userService.findByName(login);
    }

    public Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }


}
