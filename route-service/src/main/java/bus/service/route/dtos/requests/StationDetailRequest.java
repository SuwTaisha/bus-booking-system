package bus.service.route.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StationDetailRequest(@NotNull(message = "No ID provided!") Long id) {
}
