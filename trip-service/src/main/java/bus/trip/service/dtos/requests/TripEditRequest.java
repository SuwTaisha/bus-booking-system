package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.OffsetDateTime;

public record TripEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        Long vehicleId,
        OffsetDateTime departureAt,
        @PositiveOrZero(message = "Price cannot be negative!") Long price,
        Long routeId) {
}
