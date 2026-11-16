package bus.service.route.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RouteEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        String line,
        @Positive(message = "Estimated minutes must be positive!") Integer estimatedMinutes,
        @Positive(message = "Distance must be positive!") Float distance,
        Long startStationId,
        Long endStationId,
        Boolean isActive,
        String inactiveReason) {
}
