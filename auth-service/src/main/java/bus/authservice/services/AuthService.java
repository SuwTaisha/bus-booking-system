package bus.authservice.services;

import bus.authservice.dtos.requests.LoginRequest;
import bus.authservice.dtos.requests.RefreshRequest;
import bus.authservice.dtos.responses.AuthResponse;
import bus.authservice.dtos.responses.UserResponse;
import bus.authservice.exceptions.UnauthorizedException;
import bus.authservice.models.RefreshToken;
import bus.authservice.models.User;
import bus.authservice.repositories.RefreshTokenRepo;
import bus.authservice.repositories.UserRepo;

import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Invalid email or password";
    private static final String INVALID_REFRESH = "Invalid refresh token";

    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }
        if (!user.isActive()) {
            throw new ForbiddenException("Account is disabled");
        }
        return buildAuthResponse(user);
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken stored = refreshTokenRepo.findByTokenHash(tokenService.hash(request.refreshToken()))
                .orElseThrow(() -> new UnauthorizedException(INVALID_REFRESH));
        User user = stored.getUser();

        if (stored.isRevoked()) {
            refreshTokenRepo.revokeAllByUserId(user.getId(), OffsetDateTime.now());
            throw new UnauthorizedException(INVALID_REFRESH);
        }
        if (stored.isExpired()) {
            throw new UnauthorizedException("Refresh token has expired");
        }
        if (!user.isActive()) {
            throw new ForbiddenException("Account is disabled");
        }

        stored.setRevokedAt(OffsetDateTime.now());
        return buildAuthResponse(user);
    }

    @Transactional
    public void logout(RefreshRequest request) {
        refreshTokenRepo.findByTokenHash(tokenService.hash(request.refreshToken()))
                .filter(t -> !t.isRevoked())
                .ifPresent(t -> t.setRevokedAt(OffsetDateTime.now()));
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = tokenService.createAccessToken(user);
        TokenService.IssuedRefreshToken refresh = tokenService.issueRefreshToken(user);
        return new AuthResponse(accessToken, refresh.rawToken(), "Bearer",
                tokenService.accessTtlSeconds(), refresh.expiresAt(), UserResponse.from(user));
    }
}