package bus.service.route.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RouteStopEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        @Min(value = 1, message = "Stop order must be at least 1!") Short stopOrder,
        @PositiveOrZero(message = "Rest minutes cannot be negative!") Short restMinutes) {
}
