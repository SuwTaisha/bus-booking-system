package bus.trip.service.adapters;

import bus.trip.service.clients.RouteClient;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.exceptions.ServiceUnavailableException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteClientAdapter {
    private final RouteClient routeClient;

    public RouteInfoResponse getRoute(Long routeId) {
        try {
            return routeClient.getRoute(routeId).getData();
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Route not found: " + routeId);
        } catch (FeignException e) {
            log.warn("Route service call failed", e);
            throw new ServiceUnavailableException("Route service is currently unavailable");
        }
    }
}
