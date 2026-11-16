package bus.trip.service.services;

import bus.trip.service.dtos.requests.TripAddRequest;
import bus.trip.service.dtos.requests.TripEditRequest;
import bus.trip.service.dtos.requests.TripSearchRequest;
import bus.trip.service.dtos.requests.TripStatusRequest;
import bus.trip.service.dtos.responses.TripResponse;
import bus.trip.service.exceptions.BadRequestException;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import bus.trip.service.models.Vehicle;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
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

    @Transactional(readOnly = true)
    public Page<TripResponse> search(TripSearchRequest request) {
        if (request.routeId() != null && request.status() != null) {
            return tripRepo.findByRouteIdAndStatus(request.routeId(), request.status(), request.toPageable())
                    .map(TripResponse::from);
        }
        if (request.routeId() != null) {
            return tripRepo.findByRouteId(request.routeId(), request.toPageable()).map(TripResponse::from);
        }
        if (request.status() != null) {
            return tripRepo.findByStatus(request.status(), request.toPageable()).map(TripResponse::from);
        }
        return tripRepo.findAll(request.toPageable()).map(TripResponse::from);
    }

    @Transactional(readOnly = true)
    public TripResponse fetchById(Long id) {
        return TripResponse.from(findOrThrow(id));
    }

    @Transactional
    public TripResponse create(TripAddRequest request) {
        if (!request.estimatedArrivalAt().isAfter(request.departureAt())) {
            throw new BadRequestException("Estimated arrival must be after departure!");
        }
        Vehicle vehicle = findVehicleOrThrow(request.vehicleId());

        if (tripRepo.hasVehicleOverlap(vehicle.getId(), request.departureAt(), request.estimatedArrivalAt())) {
            throw new ConflictException("Vehicle is already scheduled for another trip in this time window!");
        }

        Trip trip = new Trip();
        trip.setRouteId(request.routeId());
        trip.setVehicle(vehicle);
        trip.setDepartureAt(request.departureAt());
        trip.setEstimatedArrivalAt(request.estimatedArrivalAt());
        trip.setPrice(request.price());
        trip.setStatus(TripStatus.SCHEDULED);
        return TripResponse.from(tripRepo.save(trip));
    }

    @Transactional
    public TripResponse update(TripEditRequest request) {
        Trip trip = findOrThrow(request.id());

        if (trip.getStatus() == TripStatus.COMPLETED || trip.getStatus() == TripStatus.CANCELLED) {
            throw new BadRequestException("Cannot edit a trip that is " + trip.getStatus());
        }

        if (request.vehicleId() != null) {
            trip.setVehicle(findVehicleOrThrow(request.vehicleId()));
        }
        if (request.departureAt() != null) trip.setDepartureAt(request.departureAt());
        if (request.estimatedArrivalAt() != null) trip.setEstimatedArrivalAt(request.estimatedArrivalAt());
        if (request.price() != null) trip.setPrice(request.price());

        if (!trip.getEstimatedArrivalAt().isAfter(trip.getDepartureAt())) {
            throw new BadRequestException("Estimated arrival must be after departure!");
        }

        if (tripRepo.hasVehicleOverlapExcluding(
                trip.getVehicle().getId(), trip.getDepartureAt(), trip.getEstimatedArrivalAt(), trip.getId())) {
            throw new ConflictException("Vehicle is already scheduled for another trip in this time window!");
        }

        return TripResponse.from(trip);
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
        return TripResponse.from(trip);
    }

    @Transactional
    public void delete(Long id) {
        tripRepo.delete(findOrThrow(id));
    }

    // ---------------------------------------------------------------------------

    /**
     * Allowed transitions:
     *   SCHEDULED   -> IN_PROGRESS | CANCELLED
     *   IN_PROGRESS -> COMPLETED   | CANCELLED
     */
    private void validateTransition(TripStatus current, TripStatus next) {
        boolean allowed = switch (current) {
            case SCHEDULED   -> next == TripStatus.IN_PROGRESS || next == TripStatus.CANCELLED;
            case IN_PROGRESS -> next == TripStatus.COMPLETED   || next == TripStatus.CANCELLED;
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
}
