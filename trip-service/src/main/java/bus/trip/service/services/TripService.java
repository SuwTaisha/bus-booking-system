package bus.trip.service.services;

import bus.trip.service.adapters.RouteClientAdapter;
import bus.trip.service.clients.RouteClient;
import bus.trip.service.dtos.requests.*;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.dtos.responses.TripResponse;
import bus.trip.service.exceptions.BadRequestException;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import bus.trip.service.models.Vehicle;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.TripSpecs;
import bus.trip.service.repositories.VehicleRepo;
import org.apache.hc.client5.http.RouteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepo tripRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private RouteClientAdapter routeClientAdapter;

    private static final ZoneId VN = ZoneId.of("Asia/Ho_Chi_Minh");

    @Transactional(readOnly = true)
    public Page<TripResponse> getAll(BasePageRequest request) {
        Pageable pageable = PageRequest.of(request.page(), request.size());
        Page<Trip> page = tripRepo.findAll(pageable);
        List<TripResponse> content = page.getContent().stream()
                .map(TripResponse::from)
                .toList();

        Page<TripResponse> response = new PageImpl<>(content, pageable, page.getTotalElements());
        return response;
    }

    @Transactional(readOnly = true)
    public Page<TripResponse> search(TripSearchRequest request) {
        if (request.minPrice() != null && request.maxPrice() != null
                && request.minPrice() > request.maxPrice()) {
            throw new BadRequestException("minPrice must not exceed maxPrice!");
        }

        List<Specification<Trip>> specs = new ArrayList<>();

        if (request.routeId() != null) {
            specs.add(TripSpecs.hasRoute(request.routeId()));
        }
        if (request.date() != null) {
            OffsetDateTime start = request.date().atStartOfDay(VN).toOffsetDateTime();
            OffsetDateTime end = request.date().plusDays(1).atStartOfDay(VN).toOffsetDateTime();
            specs.add(TripSpecs.departsBetween(start, end));
        }
        if (request.status() != null) {
            specs.add(TripSpecs.hasStatus(request.status()));
        } else {
            specs.add(TripSpecs.hasStatus(TripStatus.SCHEDULED));
            specs.add(TripSpecs.departsAfter(OffsetDateTime.now()));
        }
        if (request.minPrice() != null) specs.add(TripSpecs.priceAtLeast(request.minPrice()));
        if (request.maxPrice() != null) specs.add(TripSpecs.priceAtMost(request.maxPrice()));

        return tripRepo.findAll(Specification.allOf(specs), request.toPageable())
                .map(TripResponse::from);
    }

    @Transactional(readOnly = true)
    public TripResponse fetchById(Long id) {
        Trip trip = findOrThrow(id);
        RouteInfoResponse route = routeClientAdapter.getRoute(trip.getRouteId());
        return TripResponse.from(trip).withStations(route.startStationName(), route.endStationName());
    }

    @Transactional
    public TripResponse create(TripAddRequest request) {
        Vehicle vehicle = findVehicleOrThrow(request.vehicleId());
        if (!vehicle.isActive()) {
            throw new ConflictException("Vehicle is not active!");
        }

        RouteInfoResponse route = findAndCheckIsActiveRoute(request.routeId());

        OffsetDateTime arrival = request.departureAt().plusMinutes(route.estimatedMinutes());

        if (tripRepo.hasVehicleOverlap(vehicle.getId(), request.departureAt(), arrival)) {
            throw new ConflictException("Vehicle is already scheduled for another trip in this time window!");
        }

        Trip trip = new Trip();
        trip.setRouteId(request.routeId());
        trip.setVehicle(vehicle);
        trip.setDepartureAt(request.departureAt());
        trip.setEstimatedArrivalAt(arrival);
        trip.setPrice(request.price());
        trip.setStatus(TripStatus.SCHEDULED);

        return TripResponse.from(tripRepo.save(trip)).withStations(route.startStationName(), route.endStationName());

    }

    @Transactional
    public TripResponse update(Long id, TripEditRequest request) {
        Trip trip = findOrThrow(id);

        if (trip.getStatus() != TripStatus.SCHEDULED) {
            throw new ConflictException("Cannot edit a trip that is " + trip.getStatus());
        }

        if (request.vehicleId() != null) {
            Vehicle vehicle = findVehicleOrThrow(request.vehicleId());
            if (!vehicle.isActive()) {
                throw new ConflictException("Vehicle is not active!");
            }
            trip.setVehicle(vehicle);
        }
        if (request.departureAt() != null) {
            if (!request.departureAt().isAfter(OffsetDateTime.now())) {
                throw new BadRequestException("Departure must be in the future!");
            }
            trip.setDepartureAt(request.departureAt());
        }
        if (request.price() != null) {
            trip.setPrice(request.price());
        }

        Long routeId = request.routeId() != null ? request.routeId() : trip.getRouteId();
        RouteInfoResponse route = routeClientAdapter.getRoute(routeId);
        if (request.routeId() != null) {
            if (!route.isActive()) {
                throw new ConflictException("Route is not active!");
            }
            trip.setRouteId(routeId);
        }

        trip.setEstimatedArrivalAt(trip.getDepartureAt().plusMinutes(route.estimatedMinutes()));

        if (tripRepo.hasVehicleOverlapExcluding(
                trip.getVehicle().getId(), trip.getDepartureAt(), trip.getEstimatedArrivalAt(), trip.getId())) {
            throw new ConflictException("Vehicle is already scheduled for another trip in this time window!");
        }

        Trip updated = tripRepo.save(trip);
        return TripResponse.from(updated).withStations(route.startStationName(), route.endStationName());
    }

    @Transactional
    public TripResponse updateStatus(Long id, TripStatusRequest request) {
        Trip trip = findOrThrow(id);
        TripStatus next = request.status();

        validateTransition(trip.getStatus(), next);

        if (next == TripStatus.CANCELLED) {
            if (!StringUtils.hasText(request.cancelReason())) {
                throw new BadRequestException("Please provide a cancel reason!");
            }
            trip.setCancelReason(request.cancelReason().trim());
        }
        if (next == TripStatus.COMPLETED) {
            trip.setArrivedAt(OffsetDateTime.now());
        }

        trip.setStatus(next);
        return TripResponse.from(trip);
    }

    @Transactional
    public void delete(Long id) {
        tripRepo.delete(findOrThrow(id));
    }

    // ---------------------------------------------------------------------------

    /**
     * Allowed transitions:
     * SCHEDULED   -> IN_PROGRESS | CANCELLED
     * IN_PROGRESS -> COMPLETED   | CANCELLED
     */
    private void validateTransition(TripStatus current, TripStatus next) {
        boolean allowed = switch (current) {
            case SCHEDULED -> next == TripStatus.IN_PROGRESS || next == TripStatus.CANCELLED;
            case IN_PROGRESS -> next == TripStatus.COMPLETED || next == TripStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
        if (!allowed) {
            throw new BadRequestException("Cannot transition from " + current + " to " + next);
        }
    }

    private Trip findOrThrow(Long id) {
        return tripRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + id));
    }

    private Vehicle findVehicleOrThrow(Long id) {
        return vehicleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
    }

    private RouteInfoResponse findAndCheckIsActiveRoute(Long routeId) {
        RouteInfoResponse route = routeClientAdapter.getRoute(routeId);
        if (!route.isActive()) {
            throw new ConflictException("Route is not active!");
        }
        return route;
    }
}
