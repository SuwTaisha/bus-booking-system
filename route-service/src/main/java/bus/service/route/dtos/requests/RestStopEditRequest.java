package bus.service.route.dtos.requests;

import bus.service.route.models.RestStop;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

public record RestStopEditRequest(
        @NotNull(message = "No ID provided!") Long id,
        String name,
        String fullAddress,
        Boolean isActive,
        String inactiveReason) {

    public void applyTo(RestStop r) {
        if (StringUtils.hasText(name)) r.setName(name.trim());
        if (StringUtils.hasText(fullAddress)) r.setFullAddress(fullAddress.trim());
        if (isActive != null) r.setActive(isActive);
        if (StringUtils.hasText(inactiveReason)) r.setInactiveReason(inactiveReason.trim());
    }
}
