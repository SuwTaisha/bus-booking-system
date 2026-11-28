package bus.authservice.dtos.responses;

import java.time.OffsetDateTime;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           String tokenType,
                           long accessExpiresInSeconds,
                           OffsetDateTime refreshExpiresAt,
                           UserResponse user) {
}