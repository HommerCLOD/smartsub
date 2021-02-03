package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.RefreshTokenDao;
import ua.smartsub.smartsub.entity.RefreshToken;
import ua.smartsub.smartsub.exception.RefreshTokenException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {
    @Value("${app.token.refresh.duration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenDao.findByToken(token);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenDao.save(refreshToken);
    }

    public RefreshToken createRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRefreshCount(0L);
        return refreshToken;
    }
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new RefreshTokenException(token.getToken(), "Expired token. Please issue a new request");
        }
    }
    public void deleteById(Long id) {
        refreshTokenDao.deleteById(id);
    }
    public void increaseCount(RefreshToken refreshToken) {
        refreshToken.incrementRefreshCount();
        save(refreshToken);
    }

}
