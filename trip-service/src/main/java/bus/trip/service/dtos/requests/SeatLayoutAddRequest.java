package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SeatLayoutAddRequest(
        @NotNull(message = "Vehicle type ID is required!") Long vehicleTypeId,
        @NotEmpty(message = "Seat code cannot be empty!") String seatCode,
        @NotNull(message = "Deck number is required!")
        @Min(value = 1, message = "Deck number must be at least 1!") Short deckNo,
        @NotNull(message = "Row number is required!")
        @Min(value = 1, message = "Row number must be at least 1!") Short rowNo,
        @NotNull(message = "Column number is required!")
        @Min(value = 1, message = "Column number must be at least 1!") Short colNo) {
}
