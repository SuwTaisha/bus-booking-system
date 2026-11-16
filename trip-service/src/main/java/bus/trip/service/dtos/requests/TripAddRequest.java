package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.OffsetDateTime;

public record TripAddRequest(
        @NotNull(message = "Route ID is required!") Long routeId,
        @NotNull(message = "Vehicle ID is required!") Long vehicleId,
        @NotNull(message = "Departure time is required!") OffsetDateTime departureAt,
        @NotNull(message = "Estimated arrival time is required!") OffsetDateTime estimatedArrivalAt,
        @NotNull(message = "Price is required!")
        @PositiveOrZero(message = "Price cannot be negative!") Long price) {
}
