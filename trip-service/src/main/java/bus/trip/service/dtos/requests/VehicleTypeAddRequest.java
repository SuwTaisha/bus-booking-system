package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VehicleTypeAddRequest(
        @NotEmpty(message = "Type name cannot be empty!") String typeName,
        @NotNull(message = "Seat count is required!")
        @Positive(message = "Seat count must be positive!") Short seatCount,
        String description,
        Boolean isActive,
        String inactiveReason) {

    public VehicleTypeAddRequest {
        if (isActive == null) isActive = true;
    }
}
