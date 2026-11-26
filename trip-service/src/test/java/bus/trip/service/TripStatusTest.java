package bus.trip.service;

import java.util.Optional;

import bus.trip.service.adapters.RouteClientAdapter;
import bus.trip.service.dtos.requests.TripStatusRequest;
import bus.trip.service.exceptions.BadRequestException;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import bus.trip.service.models.Vehicle;
import bus.trip.service.models.VehicleType;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
import bus.trip.service.services.TripService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripStatusTest {

    @Mock
    private TripRepo tripRepo;
    @Mock
    private VehicleRepo vehicleRepo;
    @Mock
    private RouteClientAdapter routeClientAdapter;

    @InjectMocks
    private TripService tripService;

    private Trip newTrip(TripStatus status) {
        VehicleType type = new VehicleType();
        type.setTypeName("Giuong nam");

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(type);

        Trip trip = new Trip();
        trip.setId(1L);
        trip.setVehicle(vehicle);
        trip.setStatus(status);
        return trip;
    }

    @ParameterizedTest
    @CsvSource({
            "SCHEDULED, IN_PROGRESS",
            "IN_PROGRESS, COMPLETED"
    })
    void updateStatus_changesStatus_whenTransitionIsValid(TripStatus from, TripStatus to) {
        Trip trip = newTrip(from);
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));

        tripService.updateStatus(1L, new TripStatusRequest(to, null));

        assertEquals(to, trip.getStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "SCHEDULED, SCHEDULED",
            "COMPLETED, CANCELLED"
    })
    void updateStatus_throwsConflict_whenTransitionIsInvalid(TripStatus from, TripStatus to) {
        Trip trip = newTrip(from);
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));

        assertThrows(ConflictException.class,
                () -> tripService.updateStatus(1L, new TripStatusRequest(to, "any reason")));

        assertEquals(from, trip.getStatus());
    }
}