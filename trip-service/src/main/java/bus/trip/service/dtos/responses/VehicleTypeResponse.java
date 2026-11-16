package bus.trip.service.dtos.responses;

import bus.trip.service.models.VehicleType;

/** List view — no seat layout details */
public record VehicleTypeResponse(
        Long id,
        String typeName,
        short seatCount,
        String description,
        boolean isActive,
        String inactiveReason) {

    public static VehicleTypeResponse from(VehicleType vt) {
        return new VehicleTypeResponse(
                vt.getId(), vt.getTypeName(), vt.getSeatCount(),
                vt.getDescription(), vt.isActive(), vt.getInactiveReason());
    }
}
