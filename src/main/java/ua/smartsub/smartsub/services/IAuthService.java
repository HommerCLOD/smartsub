package ua.smartsub.smartsub.services;

import org.springframework.security.core.Authentication;
import ua.smartsub.smartsub.DTO.LoginDTO;
import ua.smartsub.smartsub.DTO.PasswordResetDTO;
import ua.smartsub.smartsub.DTO.RefreshTokenDTO;
import ua.smartsub.smartsub.DTO.RegisterDTO;
import ua.smartsub.smartsub.entity.EmailVerificationToken;
import ua.smartsub.smartsub.entity.RefreshToken;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.security.CustomUserDetails;

import java.util.Optional;

public interface IAuthService {
    Optional<User> registerUser(RegisterDTO userDTO);

    User findByLoginAndPassword(String username, String password);

    Optional<Authentication> authenticateUser(LoginDTO loginDTO);

    Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginDTO loginDTO);

    String generateToken(CustomUserDetails customUserDetails);

    Optional<User> confirmEmailRegistration(String emailToken);

    Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken);

    Optional<User> resetPassword(PasswordResetDTO passwordResetDTO);

    Optional<String> refreshJwtToken(RefreshTokenDTO tokenRefreshRequest);

    Boolean usernameAlreadyExists(String username);

    Boolean emailAlreadyExists(String email);
}
