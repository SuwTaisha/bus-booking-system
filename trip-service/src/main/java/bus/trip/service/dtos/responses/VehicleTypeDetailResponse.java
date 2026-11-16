package bus.trip.service.dtos.responses;

import bus.trip.service.models.VehicleType;

import java.util.List;

/** CRUD detail view — includes seat layout */
public record VehicleTypeDetailResponse(
        Long id,
        String typeName,
        short seatCount,
        String description,
        boolean isActive,
        String inactiveReason,
        List<SeatLayoutResponse> seatLayouts) {

    public static VehicleTypeDetailResponse from(VehicleType vt) {
        List<SeatLayoutResponse> layouts = vt.getSeatLayouts().stream()
                .map(SeatLayoutResponse::from)
                .toList();
        return new VehicleTypeDetailResponse(
                vt.getId(), vt.getTypeName(), vt.getSeatCount(),
                vt.getDescription(), vt.isActive(), vt.getInactiveReason(),
                layouts);
    }
}
