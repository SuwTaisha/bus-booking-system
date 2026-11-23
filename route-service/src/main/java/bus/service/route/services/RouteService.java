package bus.service.route.services;

import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.RouteAddRequest;
import bus.service.route.dtos.requests.RouteEditRequest;
import bus.service.route.dtos.responses.RouteDetailResponse;
import bus.service.route.dtos.responses.RouteResponse;
import bus.service.route.exceptions.ConflictException;
import bus.service.route.exceptions.ResourceNotFoundException;
import bus.service.route.models.Route;
import bus.service.route.models.Station;
import bus.service.route.repositories.RouteRepo;
import bus.service.route.repositories.StationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Service for routes: fetch, create, update, delete.
 * Enforces the unique active (startStation, endStation, line) constraint at application level
 * in addition to the DB partial unique index.
 */
@Service
public class RouteService {

    @Autowired
    private RouteRepo routeRepo;

    @Autowired
    private StationRepo stationRepo;


    @Transactional(readOnly = true)
    public List<RouteResponse> fetchByBatch(List<Long> id) {
        List<RouteResponse> response = routeRepo.findByIdIn(id).stream().map(RouteResponse::from).toList();
        return response;
    }

    @Transactional(readOnly = true)
    public Page<RouteResponse> fetchAll(BasePageRequest request) {
        Pageable pageable = request.toPageable(Sort.by("line"));
        return routeRepo.findByActiveTrue(pageable).map(RouteResponse::from);
    }

    @Transactional(readOnly = true)
    public RouteDetailResponse fetchById(Long id) {
        Route route = findRouteOrThrow(id);
        return RouteDetailResponse.from(route);
    }

    @Transactional
    public RouteDetailResponse create(RouteAddRequest request) {
        if (request.startStationId().equals(request.endStationId())) {
            throw new ConflictException("Start station and end station must be different!");
        }

        Station start = findStationOrThrow(request.startStationId());
        Station end = findStationOrThrow(request.endStationId());

        boolean duplicate = routeRepo.existsByStartStationIdAndEndStationIdAndLineAndActiveTrue(
                start.getId(), end.getId(), request.line().trim());
        if (duplicate) {
            throw new ConflictException("An active route with the same start, end and line already exists!");
        }

        Route route = new Route();
        route.setLine(request.line().trim());
        route.setEstimatedMinutes(request.estimatedMinutes());
        route.setDistance(request.distance());
        route.setStartStation(start);
        route.setEndStation(end);
        route.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) route.setInactiveReason(request.inactiveReason().trim());

        return RouteDetailResponse.from(routeRepo.save(route));
    }

    @Transactional
    public RouteDetailResponse update(RouteEditRequest request) {
        Route route = findRouteOrThrow(request.id());

        if (request.startStationId() != null) {
            route.setStartStation(findStationOrThrow(request.startStationId()));
        }
        if (request.endStationId() != null) {
            route.setEndStation(findStationOrThrow(request.endStationId()));
        }
        if (StringUtils.hasText(request.line())) {
            route.setLine(request.line().trim());
        }
        if (request.estimatedMinutes() != null) route.setEstimatedMinutes(request.estimatedMinutes());
        if (request.distance() != null) route.setDistance(request.distance());
        if (request.isActive() != null) route.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) route.setInactiveReason(request.inactiveReason().trim());

        // Re-check uniqueness after applying changes
        if (route.getStartStation().getId().equals(route.getEndStation().getId())) {
            throw new ConflictException("Start station and end station must be different!");
        }
        boolean duplicate = routeRepo.existsActiveDuplicateExcluding(
                route.getStartStation().getId(), route.getEndStation().getId(), route.getLine(), route.getId());
        if (duplicate) {
            throw new ConflictException("An active route with the same start, end and line already exists!");
        }

        return RouteDetailResponse.from(route);
    }

    @Transactional
    public void delete(Long id) {
        routeRepo.delete(findRouteOrThrow(id));
    }

    // ---------------------------------------------------------------------------

    private Route findRouteOrThrow(Long id) {
        return routeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + id));
    }

    private Station findStationOrThrow(Long id) {
        return stationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found: " + id));
    }
}
