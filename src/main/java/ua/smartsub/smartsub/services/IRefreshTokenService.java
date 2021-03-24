package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.model.entity.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {
    void deleteById(Long id);

    void increaseCount(RefreshToken refreshToken);

    void verifyExpiration(RefreshToken token);

    RefreshToken createRefreshToken();

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);
}
