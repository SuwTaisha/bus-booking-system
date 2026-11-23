package bus.trip.service.dtos.requests;

import bus.trip.service.models.TripStatus;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record TripStatusRequest(
        @NotNull(message = "No ID provided!") Long id,
        @NotNull(message = "Status is required!") TripStatus status,
        String cancelReason) {
}
