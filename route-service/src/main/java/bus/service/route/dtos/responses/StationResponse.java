package bus.service.route.dtos.responses;

import bus.service.route.models.Station;

public record StationResponse(Long id, String name, String province, String district, String fullAddress,
                              boolean isActive, String inactiveReason) {
    public static StationResponse fromStation(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getProvince(), station.getDistrict(), station.getFullAddress(), station.isActive(), station.getInactiveReason());
    }
}
