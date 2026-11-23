package bus.trip.service.dtos.responses;

import bus.trip.service.models.Trip;
import bus.trip.service.models.VehicleType;

import java.util.List;

public record TripSeatMapResponse(Long tripId, String vehicleTypeName, int totalSeats,
                                  List<SeatLayoutResponse> seats) {

    public static TripSeatMapResponse from(Trip t, List<SeatLayoutResponse> seats) {
        VehicleType type = t.getVehicle().getVehicleType();
        return new TripSeatMapResponse(t.getId(), type.getTypeName(), seats.size(), seats);
    }
}