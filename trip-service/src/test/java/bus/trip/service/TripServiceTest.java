package bus.trip.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import bus.trip.service.adapters.RouteClientAdapter;
import bus.trip.service.dtos.requests.TripAddRequest;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.models.Vehicle;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
import bus.trip.service.services.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TripServiceTest {
    @Mock
    private TripRepo tripRepo;
    @Mock
    private VehicleRepo vehicleRepo;
    @Mock
    private RouteClientAdapter routeClientAdapter;

    @InjectMocks
    private TripService tripService;

    private RouteInfoResponse testInactiveRoute;

    private Vehicle vehicle;
    private Vehicle inactiveVehicle;
    private TripAddRequest tripAddRequest;

    //Test data
    @BeforeEach
    void attachMocks() {
        vehicle = new Vehicle();
        vehicle.setActive(true);
        inactiveVehicle = new Vehicle();
        inactiveVehicle.setActive(false);
        tripAddRequest = new TripAddRequest(1L, 3L, OffsetDateTime.now().plusDays(1), 180000L);
        testInactiveRoute = new RouteInfoResponse(1L, "Quốc lộ 14", 120, "A", "B", false);
    }

    @Test
    void create_throwsConflict_whenRouteIsInactive() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L)).thenReturn(testInactiveRoute);
        ConflictException result = assertThrows(ConflictException.class, () -> tripService.create(tripAddRequest));
        assertEquals("Route is not active!", result.getMessage());
        verify(tripRepo, never()).save(any());
    }

    @Test
    void create_throwsConflict_whenVehicleIsInactive() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(inactiveVehicle));
        ConflictException result = assertThrows(ConflictException.class, () -> tripService.create(tripAddRequest));
        assertEquals("Vehicle is not active!", result.getMessage());
        verifyNoInteractions(routeClientAdapter);
        verify(tripRepo, never()).save(any());
    }

    @Test
    void create_throwsConflict_whenRouteIsNotExisted() {

    }


}
