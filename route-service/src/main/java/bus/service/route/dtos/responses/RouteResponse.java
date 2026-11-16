package bus.service.route.dtos.responses;

import bus.service.route.models.Route;

/** List view — does not include stops */
public record RouteResponse(
        Long id,
        String line,
        int estimatedMinutes,
        float distance,
        Long startStationId,
        String startStationName,
        Long endStationId,
        String endStationName,
        boolean isActive,
        String inactiveReason) {

    public static RouteResponse from(Route r) {
        return new RouteResponse(
                r.getId(),
                r.getLine(),
                r.getEstimatedMinutes(),
                r.getDistance(),
                r.getStartStation().getId(),
                r.getStartStation().getName(),
                r.getEndStation().getId(),
                r.getEndStation().getName(),
                r.isActive(),
                r.getInactiveReason());
    }
}
