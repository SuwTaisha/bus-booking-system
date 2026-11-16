package bus.service.route.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RouteStopAddRequest(
        @NotNull(message = "Route ID is required!") Long routeId,
        @NotNull(message = "Rest stop ID is required!") Long restStopId,
        @NotNull(message = "Stop order is required!")
        @Min(value = 1, message = "Stop order must be at least 1!") Short stopOrder,
        @NotNull(message = "Rest minutes is required!")
        @PositiveOrZero(message = "Rest minutes cannot be negative!") Short restMinutes) {
}
