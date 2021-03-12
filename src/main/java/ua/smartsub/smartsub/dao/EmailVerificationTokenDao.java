package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.model.entity.EmailVerificationToken;

import java.util.Optional;


public interface EmailVerificationTokenDao extends JpaRepository<EmailVerificationToken, Long> {

    @Query("select e from EmailVerificationToken e where e.token=:token ")
    Optional<EmailVerificationToken> findByToken(String token);
}
