package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VehicleEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        Long vehicleTypeId,
        String licensePlate,
        String model,
        @Min(value = 1980, message = "Manufacture year must be >= 1980!")
        @Max(value = 2100, message = "Manufacture year must be <= 2100!") Integer manufactureYear,
        Boolean isActive,
        String inactiveReason) {
}
