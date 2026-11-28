package bus.authservice.services;

import bus.authservice.models.RefreshToken;
import bus.authservice.models.User;
import bus.authservice.repositories.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class TokenService {

    public record IssuedRefreshToken(String rawToken, OffsetDateTime expiresAt) {}

    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepo refreshTokenRepo;
    private final long accessTtlMinutes;
    private final long refreshTtlDays;
    private final SecureRandom secureRandom = new SecureRandom();

    public TokenService(JwtEncoder jwtEncoder,
                        RefreshTokenRepo refreshTokenRepo,
                        @Value("${app.jwt.access-ttl-minutes:15}") long accessTtlMinutes,
                        @Value("${app.jwt.refresh-ttl-days:7}") long refreshTtlDays) {
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenRepo = refreshTokenRepo;
        this.accessTtlMinutes = accessTtlMinutes;
        this.refreshTtlDays = refreshTtlDays;
    }

    public long accessTtlSeconds() {
        return accessTtlMinutes * 60;
    }

    public String createAccessToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("bus-auth-service")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plus(accessTtlMinutes, ChronoUnit.MINUTES))
                .claim("role", user.getRole().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public IssuedRefreshToken issueRefreshToken(User user) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(hash(rawToken));
        entity.setExpiresAt(OffsetDateTime.now().plusDays(refreshTtlDays));
        refreshTokenRepo.save(entity);

        return new IssuedRefreshToken(rawToken, entity.getExpiresAt());
    }

    public String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}