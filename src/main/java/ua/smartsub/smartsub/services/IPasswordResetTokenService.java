package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.model.entity.PasswordResetToken;

import java.util.Optional;

public interface IPasswordResetTokenService {
    void verifyExpiration(PasswordResetToken token);

    PasswordResetToken createToken();

    Optional<PasswordResetToken> findByToken(String token);

    PasswordResetToken save(PasswordResetToken passwordResetToken);
}
