package ua.smartsub.smartsub.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.smartsub.smartsub.exception.InvalidTokenRequestException;
import ua.smartsub.smartsub.security.CustomUserDetails;

import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpirationInMs;

    @Value("${app.jwt.claims.refresh.name}")
    private String jwtClaimRefreshName;

    public String generateToken(CustomUserDetails customUserDetails) {
        return generateAccessToken(customUserDetails.getUsername());
    }


    public String generateAccessToken(String login) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
            throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required.");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getExpiryDuration() {
        return jwtExpirationInMs;
    }
}
