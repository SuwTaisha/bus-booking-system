package bus.service.route.dtos.responses;

public record StationResponse(Long id, String name, String province, String district, String fullAddress,
                              boolean isActive, String inactiveReason) {

}
