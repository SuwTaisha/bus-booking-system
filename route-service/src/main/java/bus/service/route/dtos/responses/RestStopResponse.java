package bus.service.route.dtos.responses;

import bus.service.route.models.RestStop;

public record RestStopResponse(Long id, String name, String fullAddress, boolean isActive, String inactiveReason) {

    public static RestStopResponse from(RestStop r) {
        return new RestStopResponse(r.getId(), r.getName(), r.getFullAddress(), r.isActive(), r.getInactiveReason());
    }
}
