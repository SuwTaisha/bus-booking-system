package bus.service.route.dtos.responses;

import bus.service.route.models.Route;

import java.util.List;

/** CRUD detail view — includes the ordered stop list */
public record RouteDetailResponse(
        Long id,
        String line,
        int estimatedMinutes,
        float distance,
        Long startStationId,
        String startStationName,
        Long endStationId,
        String endStationName,
        boolean isActive,
        String inactiveReason,
        List<RouteStopResponse> stops) {

    public static RouteDetailResponse from(Route r) {
        List<RouteStopResponse> stopList = r.getStops().stream()
                .map(RouteStopResponse::from)
                .toList();
        return new RouteDetailResponse(
                r.getId(),
                r.getLine(),
                r.getEstimatedMinutes(),
                r.getDistance(),
                r.getStartStation().getId(),
                r.getStartStation().getName(),
                r.getEndStation().getId(),
                r.getEndStation().getName(),
                r.isActive(),
                r.getInactiveReason(),
                stopList);
    }
}
