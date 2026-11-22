package bus.trip.service.clients;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "route-service")
public interface RouteClient {
    @GetMapping("/api/route/routes/{id}")
    ApiResponse<RouteInfoResponse> getRoute(@PathVariable("id") Long id);
}
