package bus.trip.service.dtos.responses;

import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;

import java.time.OffsetDateTime;

public record TripResponse(
        Long id,
        Long routeId,
        Long vehicleId,
        String licensePlate,
        String vehicleTypeName,
        OffsetDateTime departureAt,
        OffsetDateTime estimatedArrivalAt,
        OffsetDateTime arrivedAt,
        long price,
        TripStatus status,
        String cancelReason, String startStation, String endStation) {

    public static TripResponse from(Trip t, String startStation, String endStation) {
        return new TripResponse(
                t.getId(),
                t.getRouteId(),
                t.getVehicle().getId(),
                t.getVehicle().getLicensePlate(),
                t.getVehicle().getVehicleType().getTypeName(),
                t.getDepartureAt(),
                t.getEstimatedArrivalAt(),
                t.getArrivedAt(),
                t.getPrice(),
                t.getStatus(),
                t.getCancelReason(), startStation, endStation);
    }

    public TripResponse withStations(String start, String end) {
        return new TripResponse(id, routeId, vehicleId, licensePlate, vehicleTypeName,
                departureAt, estimatedArrivalAt, arrivedAt, price, status,
                cancelReason, start, end);
    }
}
