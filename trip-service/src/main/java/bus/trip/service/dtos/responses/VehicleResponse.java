package bus.trip.service.dtos.responses;

import bus.trip.service.models.Vehicle;

public record VehicleResponse(
        Long id,
        Long vehicleTypeId,
        String vehicleTypeName,
        String licensePlate,
        String model,
        Integer manufactureYear,
        boolean isActive,
        String inactiveReason) {

    public static VehicleResponse from(Vehicle v) {
        return new VehicleResponse(
                v.getId(),
                v.getVehicleType().getId(),
                v.getVehicleType().getTypeName(),
                v.getLicensePlate(),
                v.getModel(),
                v.getManufactureYear(),
                v.isActive(),
                v.getInactiveReason());
    }
}
