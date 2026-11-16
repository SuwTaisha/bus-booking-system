package bus.service.route.services;

import bus.service.route.dtos.requests.RouteStopAddRequest;
import bus.service.route.dtos.requests.RouteStopEditRequest;
import bus.service.route.dtos.responses.RouteStopResponse;
import bus.service.route.exceptions.ConflictException;
import bus.service.route.exceptions.ResourceNotFoundException;
import bus.service.route.models.RestStop;
import bus.service.route.models.Route;
import bus.service.route.models.RouteStop;
import bus.service.route.repositories.RestStopRepo;
import bus.service.route.repositories.RouteRepo;
import bus.service.route.repositories.RouteStopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for route stops: fetch, add, edit, remove.
 * Enforces unique (route_id, stop_order) and unique (route_id, rest_stop_id) constraints.
 */
@Service
public class RouteStopService {

    @Autowired
    private RouteStopRepo routeStopRepo;

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private RestStopRepo restStopRepo;

    @Transactional(readOnly = true)
    public List<RouteStopResponse> fetchByRoute(Long routeId) {
        findRouteOrThrow(routeId);
        return routeStopRepo.findByRouteIdOrderByStopOrderAsc(routeId)
                .stream()
                .map(RouteStopResponse::from)
                .toList();
    }

    @Transactional
    public RouteStopResponse add(RouteStopAddRequest request) {
        Route route = findRouteOrThrow(request.routeId());
        RestStop restStop = findRestStopOrThrow(request.restStopId());

        if (routeStopRepo.existsByRouteIdAndStopOrder(route.getId(), request.stopOrder())) {
            throw new ConflictException("Stop order " + request.stopOrder() + " already used on this route!");
        }
        if (routeStopRepo.existsByRouteIdAndRestStopId(route.getId(), restStop.getId())) {
            throw new ConflictException("This rest stop is already assigned to the route!");
        }

        RouteStop routeStop = new RouteStop();
        routeStop.setRoute(route);
        routeStop.setRestStop(restStop);
        routeStop.setStopOrder(request.stopOrder());
        routeStop.setRestMinutes(request.restMinutes());

        return RouteStopResponse.from(routeStopRepo.save(routeStop));
    }

    @Transactional
    public RouteStopResponse edit(RouteStopEditRequest request) {
        RouteStop routeStop = routeStopRepo.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Route stop not found: " + request.id()));

        if (request.stopOrder() != null) {
            boolean orderTaken = routeStopRepo.existsByRouteIdAndStopOrderAndIdNot(
                    routeStop.getRoute().getId(), request.stopOrder(), routeStop.getId());
            if (orderTaken) {
                throw new ConflictException("Stop order " + request.stopOrder() + " already used on this route!");
            }
            routeStop.setStopOrder(request.stopOrder());
        }
        if (request.restMinutes() != null) {
            routeStop.setRestMinutes(request.restMinutes());
        }

        return RouteStopResponse.from(routeStop);
    }

    @Transactional
    public void remove(Long id) {
        RouteStop routeStop = routeStopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route stop not found: " + id));
        routeStopRepo.delete(routeStop);
    }

    // ---------------------------------------------------------------------------

    private Route findRouteOrThrow(Long id) {
        return routeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    private RestStop findRestStopOrThrow(Long id) {
        return restStopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rest stop not found: " + id));
    }
}
