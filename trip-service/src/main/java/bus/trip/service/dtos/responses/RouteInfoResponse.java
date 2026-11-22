package bus.trip.service.dtos.responses;

public record RouteInfoResponse(Long id, String line, int estimatedMinutes,
                                String startStationName, String endStationName,
                                boolean isActive) {
}
