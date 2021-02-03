package ua.smartsub.smartsub.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.smartsub.smartsub.DTO.ApiResponse;
import ua.smartsub.smartsub.DTO.LogOutDTO;
import ua.smartsub.smartsub.DTO.UpdatePasswordDTO;
import ua.smartsub.smartsub.annotation.CurrentUser;
import ua.smartsub.smartsub.event.OnUserAccountChangeEvent;
import ua.smartsub.smartsub.exception.UpdatePasswordException;
import ua.smartsub.smartsub.security.CustomUserDetails;
import ua.smartsub.smartsub.services.implentation.AuthService;
import ua.smartsub.smartsub.services.implentation.UserService;

import javax.validation.Valid;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Returns the current user profile")
    public ResponseEntity getUserProfile(@CurrentUser CustomUserDetails currentUser) {
        log.info(currentUser.getEmail() + " has role: " + currentUser.getRole());
        return ResponseEntity.ok("Hello. This is about me");
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Returns the list of configured admins. Requires ADMIN Access")
    public ResponseEntity getAllAdmins() {
        log.info("Inside secured resource with admin");
        return ResponseEntity.ok("Hello. This is about admins");
    }


    @PostMapping("/password/update")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
            "password")
    public ResponseEntity updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
                                             @ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {

        return authService.updatePassword(customUserDetails, updatePasswordDTO)
                .map(updatedUser -> {
                    OnUserAccountChangeEvent onUserPasswordChangeEvent = new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
                    applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
                })
                .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
    }

    /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logoutUser(@CurrentUser CustomUserDetails customUserDetails,
                                     @ApiParam(value = "The LogOutRequest payload") @Valid @RequestBody LogOutDTO logOutDTO) {
        userService.logoutUser(customUserDetails, logOutDTO);
        return ResponseEntity.ok(new ApiResponse("Log out successful", true));
    }


}
