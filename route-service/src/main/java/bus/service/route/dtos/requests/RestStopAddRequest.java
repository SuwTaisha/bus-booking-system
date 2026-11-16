package bus.service.route.dtos.requests;

import bus.service.route.models.RestStop;
import jakarta.validation.constraints.NotEmpty;

public record RestStopAddRequest(
        @NotEmpty(message = "Rest stop name cannot be empty!") String name,
        @NotEmpty(message = "Rest stop address cannot be empty!") String fullAddress,
        Boolean isActive,
        String inactiveReason) {

    public RestStopAddRequest {
        if (isActive == null) isActive = true;
    }

    public RestStop toEntity() {
        RestStop r = new RestStop();
        r.setName(name.trim());
        r.setFullAddress(fullAddress.trim());
        r.setActive(isActive);
        if (inactiveReason != null) r.setInactiveReason(inactiveReason.trim());
        return r;
    }
}
