package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.PasswordResetTokenDao;
import ua.smartsub.smartsub.entity.PasswordResetToken;
import ua.smartsub.smartsub.exception.InvalidTokenRequestException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenDao passwordResetTokenDao;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;



    /**
     * Saves the given password reset token
     */
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenDao.save(passwordResetToken);
    }

    /**
     * Finds a token in the database given its naturalId
     */
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenDao.findByToken(token);
    }

    /**
     * Creates and returns a new password token to which a user must be associated
     */
    public PasswordResetToken createToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        String token = UUID.randomUUID().toString();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
        return passwordResetToken;
    }

    /**
     * Verify whether the token provided has expired or not on the basis of the current
     * server time and/or throw error otherwise
     */
    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
                    "Expired token. Please issue a new request");
        }
    }
}
