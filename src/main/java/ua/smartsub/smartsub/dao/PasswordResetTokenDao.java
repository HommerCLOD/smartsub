package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.smartsub.smartsub.model.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenDao extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
}
