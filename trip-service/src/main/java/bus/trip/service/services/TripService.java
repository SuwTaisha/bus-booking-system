package bus.trip.service.services;

import bus.trip.service.adapters.RouteClientAdapter;
import bus.trip.service.clients.RouteClient;
import bus.trip.service.dtos.requests.TripAddRequest;
import bus.trip.service.dtos.requests.TripEditRequest;
import bus.trip.service.dtos.requests.TripSearchRequest;
import bus.trip.service.dtos.requests.TripStatusRequest;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.dtos.responses.TripResponse;
import bus.trip.service.exceptions.BadRequestException;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import bus.trip.service.models.Vehicle;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
import org.apache.hc.client5.http.RouteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class TripService {

    @Autowired
    private TripRepo tripRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private RouteClientAdapter routeClientAdapter;
//    @Transactional(readOnly = true)
//    public Page<TripResponse> search(TripSearchRequest request) {
//        if (request.routeId() != null && request.status() != null) {
//            return tripRepo.findByRouteIdAndStatus(request.routeId(), request.status(), request.toPageable())
//                    .map(TripResponse::from);
//        }
//        if (request.routeId() != null) {
//            return tripRepo.findByRouteId(request.routeId(), request.toPageable()).map(TripResponse::from);
//        }
//        if (request.status() != null) {
//            return tripRepo.findByStatus(request.status(), request.toPageable()).map(TripResponse::from);
//        }
//        return tripRepo.findAll(request.toPageable()).map(TripResponse::from);
//    }

    @Transactional(readOnly = true)
    public TripResponse fetchById(Long id) {
        Trip trip = findOrThrow(id);
        RouteInfoResponse route = routeClientAdapter.getRoute(trip.getRouteId());
        return TripResponse.from(trip, route.startStationName(), route.endStationName());
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

        return TripResponse.from(tripRepo.save(trip), route.startStationName(), route.endStationName());
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
        return TripResponse.from(updated, route.startStationName(), route.endStationName());
    }

    @Transactional
    public TripResponse updateStatus(TripStatusRequest request) {
        Trip trip = findOrThrow(request.id());
        TripStatus next = request.status();

        validateTransition(trip.getStatus(), next);

        trip.setStatus(next);
        if (next == TripStatus.CANCELLED) {
            trip.setCancelReason(request.cancelReason());
        }
        if (next == TripStatus.COMPLETED) {
            trip.setArrivedAt(OffsetDateTime.now());
        }
        RouteInfoResponse route = routeClientAdapter.getRoute(trip.getRouteId());
        return TripResponse.from(trip, route.startStationName(), route.endStationName());
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
