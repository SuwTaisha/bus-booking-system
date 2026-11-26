package bus.trip.service.adapters;

import bus.trip.service.clients.RouteClient;
import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.exceptions.ServiceUnavailableException;
import feign.FeignException;
import feign.codec.DecodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RouteClientAdapter {
    private final RouteClient routeClient;

    public RouteInfoResponse getRoute(Long routeId) {
        try {
            try {
                return routeClient.getRoute(routeId).getData();
            } catch (FeignException.NotFound e) {
                throw new ResourceNotFoundException("Route not found: " + routeId);
            } catch (FeignException e) {
                log.warn("Route service call failed", e);
                throw new ServiceUnavailableException("Route service is currently unavailable");
            }
        } catch (DecodeException e) {
            log.warn("Route service call failed, decode exception", e);
            throw new ServiceUnavailableException("Route service is currently unavailable");
        }
    }


    public Map<Long, RouteInfoResponse> getRoutesOrEmpty(Collection<Long> routeIds) {
        if (routeIds == null || routeIds.isEmpty()) {
            return Map.of();
        }
        try {
            List<RouteInfoResponse> data = routeClient.getRoutes(List.copyOf(routeIds)).getData();
            if (data == null) {
                return Map.of();
            }
            return data.stream()
                    .collect(Collectors.toMap(RouteInfoResponse::id, r -> r, (a, b) -> a));
        } catch (FeignException e) {
            log.warn("Batch route lookup failed, returning trips without station names", e);
            return Map.of();
        }
    }
}
