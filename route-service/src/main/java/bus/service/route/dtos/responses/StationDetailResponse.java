package bus.service.route.dtos.responses;

import bus.service.route.models.Station;


/**
 * This Record is Used for the data structure of CRUD.
 * Every Admin CRUD Action will get this response
 * @param id
 * @param name
 * @param province
 * @param district
 * @param coordinate
 * @param fullAddress
 * @param isActive
 * @param inactiveReason
 */
public record StationDetailResponse(Long id, String name, String province, String district, String coordinate,
                                    String fullAddress, boolean isActive, String inactiveReason) {
    public static StationDetailResponse fromStation(Station station) {
        return new StationDetailResponse(station.getId(), station.getName(), station.getProvince(), station.getDistrict(), station.getCoordinate(), station.getFullAddress(), station.isActive(), station.getInactiveReason());
    }

}
