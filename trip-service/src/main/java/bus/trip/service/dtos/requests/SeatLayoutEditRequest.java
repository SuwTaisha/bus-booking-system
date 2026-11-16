package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SeatLayoutEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        String seatCode,
        @Min(value = 1, message = "Deck number must be at least 1!") Short deckNo,
        @Min(value = 1, message = "Row number must be at least 1!") Short rowNo,
        @Min(value = 1, message = "Column number must be at least 1!") Short colNo) {
}
