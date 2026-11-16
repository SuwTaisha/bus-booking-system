package bus.service.route.dtos.responses;

import bus.service.route.models.RouteStop;

public record RouteStopResponse(
        Long id,
        Long restStopId,
        String restStopName,
        String restStopAddress,
        short stopOrder,
        short restMinutes) {

    public static RouteStopResponse from(RouteStop rs) {
        return new RouteStopResponse(
                rs.getId(),
                rs.getRestStop().getId(),
                rs.getRestStop().getName(),
                rs.getRestStop().getFullAddress(),
                rs.getStopOrder(),
                rs.getRestMinutes());
    }
}
