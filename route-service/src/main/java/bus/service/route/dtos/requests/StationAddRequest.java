package bus.service.route.dtos.requests;

import bus.service.route.models.Station;
import jakarta.validation.constraints.NotEmpty;

public record StationAddRequest(@NotEmpty(message = "Station name cannot be empty!") String name,
                                @NotEmpty(message = "Province cannot be empty!") String province,
                                @NotEmpty(message = "District cannot be empty!") String district, String coordinate,
                                @NotEmpty(message = "Station Address cannot be empty!") String fullAddress,
                                Boolean isActive, String inactiveReason) {
    public StationAddRequest {
        if (isActive == null) isActive = true;
    }

    public Station toEntity() {
        Station s = new Station();
        s.setName(name);
        s.setProvince(province);
        s.setDistrict(district);
        s.setFullAddress(fullAddress);
        s.setCoordinate(coordinate);
        return s;
    }

}
