package bus.service.route.dtos.requests;

import bus.service.route.models.Station;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

public record StationEditRequest(@NotNull(message = "No ID provided!") Long id, String name,
                                 String province,
                                 String district, String coordinate,
                                 String fullAddress,
                                 Boolean isActive, String inactiveReason) {

    public void applyTo(Station s) {
        if (StringUtils.hasText(name)) s.setName(name.trim());
        if (StringUtils.hasText(province)) s.setProvince(province.trim());
        if (StringUtils.hasText(district)) s.setDistrict(district.trim());
        if (StringUtils.hasText(fullAddress)) s.setFullAddress(fullAddress.trim());
        if (StringUtils.hasText(coordinate)) s.setCoordinate(coordinate.trim());
    }
}
