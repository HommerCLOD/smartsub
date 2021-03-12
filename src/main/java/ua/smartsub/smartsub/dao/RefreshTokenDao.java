package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.model.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenDao extends JpaRepository<RefreshToken,Long> {
    @Query("select r from REFRESH_TOKEN r where r.token=:token ")
    Optional<RefreshToken> findByToken(String token);

}
