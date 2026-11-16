package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleTypeEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        String typeName,
        @Positive(message = "Seat count must be positive!") Short seatCount,
        String description,
        Boolean isActive,
        String inactiveReason) {
}
