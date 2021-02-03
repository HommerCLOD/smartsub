package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.smartsub.smartsub.DTO.*;
import ua.smartsub.smartsub.dao.RoleDao;
import ua.smartsub.smartsub.entity.*;
import ua.smartsub.smartsub.exception.ResourceNotFoundException;
import ua.smartsub.smartsub.exception.TokenRefreshException;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.exception.UpdatePasswordException;
import ua.smartsub.smartsub.security.CustomUserDetails;
import ua.smartsub.smartsub.security.jwt.JwtProvider;
import ua.smartsub.smartsub.services.IAuthService;

import java.util.Optional;

@Service
@Slf4j
public class AuthService implements IAuthService {


    @Autowired
    private UserService userService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private EmailVerificationTokenService emailVerificationTokenService;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Override
    public Optional<User> registerUser(RegisterDTO userDTO) {
        if (usernameAlreadyExists(userDTO.getUsername())) {
            log.error("Username already exists: " + userDTO.getUsername());
            throw new UniqueUserException("Юзер з таким логіном вже існує");
        } else if (emailAlreadyExists(userDTO.getEmail())) {
            log.error("Email already exists: " + userDTO.getEmail());
            throw new UniqueUserException("Юзер з таким е-маіл вже існує");
        }
        log.info("Trying to register new user [" + userDTO.getUsername() + "]");
        Role userRole = roleDao.findByName("ROLE_USER");
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmailVerified(false);
        return Optional.ofNullable(userService.save(user));
    }

    public User findByLoginAndPassword(String username, String password) {
        User user = userService.findByName(username);
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

    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginDTO loginDTO) {
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
        return Optional.ofNullable(refreshToken);
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

    public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Existing email verification", existingToken));

        if (emailVerificationToken.getUser().getEmailVerified()) {
            return Optional.empty();
        }
        return Optional.ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
    }

    public Optional<User> resetPassword(PasswordResetDTO passwordResetDTO) {
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
                });
    }

    public Optional<String> refreshJwtToken(RefreshTokenDTO tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

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
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Missing refresh token in database.Please login again"));
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

    public User findByLogin(String login) {
        return userService.findByName(login);
    }

    public Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }


}
