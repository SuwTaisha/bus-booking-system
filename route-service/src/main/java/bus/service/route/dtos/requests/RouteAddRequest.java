package bus.service.route.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RouteAddRequest(
        @NotEmpty(message = "Route line cannot be empty!") String line,
        @NotNull(message = "Estimated minutes is required!")
        @Positive(message = "Estimated minutes must be positive!") Integer estimatedMinutes,
        @NotNull(message = "Distance is required!")
        @Positive(message = "Distance must be positive!") Float distance,
        @NotNull(message = "Start station ID is required!") Long startStationId,
        @NotNull(message = "End station ID is required!") Long endStationId,
        Boolean isActive,
        String inactiveReason) {

    public RouteAddRequest {
        if (isActive == null) isActive = true;
    }
}
