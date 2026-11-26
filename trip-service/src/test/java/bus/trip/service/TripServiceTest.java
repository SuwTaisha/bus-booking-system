package bus.trip.service;

import java.time.OffsetDateTime;
import java.util.Optional;

import bus.trip.service.adapters.RouteClientAdapter;
import bus.trip.service.dtos.requests.TripAddRequest;
import bus.trip.service.dtos.responses.RouteInfoResponse;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.exceptions.ServiceUnavailableException;
import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import bus.trip.service.models.Vehicle;
import bus.trip.service.models.VehicleType;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
import bus.trip.service.services.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Captor
    private ArgumentCaptor<Trip> tripCaptor;

    private Vehicle vehicle;
    private Vehicle inactiveVehicle;
    private VehicleType vehicleType;
    private RouteInfoResponse activeRoute;
    private RouteInfoResponse inactiveRoute;
    private TripAddRequest tripAddRequest;

    @BeforeEach
    void setUp() {
        vehicleType = new VehicleType();
        vehicleType.setTypeName("Giuong nam");

        vehicle = new Vehicle();
        vehicle.setActive(true);
        vehicle.setVehicleType(vehicleType);

        inactiveVehicle = new Vehicle();
        inactiveVehicle.setActive(false);

        activeRoute = new RouteInfoResponse(1L, "Quốc lộ 14", 120, "A", "B", true);
        inactiveRoute = new RouteInfoResponse(1L, "Quốc lộ 14", 120, "A", "B", false);

        tripAddRequest = new TripAddRequest(1L, 3L, OffsetDateTime.now().plusDays(1), 180000L);
    }

    @Test
    void create_throwsConflict_whenRouteIsInactive() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L)).thenReturn(inactiveRoute);

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
    void create_throwsNotFound_whenRouteDoesNotExist() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L)).thenThrow(new ResourceNotFoundException("Route not found!"));

        ResourceNotFoundException result =
                assertThrows(ResourceNotFoundException.class, () -> tripService.create(tripAddRequest));

        assertEquals("Route not found!", result.getMessage());
        verify(tripRepo, never()).save(any());
    }

    @Test
    void create_throwsServiceUnavailable_whenRouteServiceIsDown() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L))
                .thenThrow(new ServiceUnavailableException("Route service is currently unavailable"));

        ServiceUnavailableException result =
                assertThrows(ServiceUnavailableException.class, () -> tripService.create(tripAddRequest));

        assertEquals("Route service is currently unavailable", result.getMessage());
        verify(tripRepo, never()).save(any());
    }

    @Test
    void create_throwsConflict_whenVehicleHasOverlap() {
        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L)).thenReturn(activeRoute);
        when(tripRepo.hasVehicleOverlap(any(), any(), any())).thenReturn(true);

        ConflictException result = assertThrows(ConflictException.class, () -> tripService.create(tripAddRequest));

        assertEquals("Vehicle is already scheduled for another trip in this time window!", result.getMessage());
        verify(tripRepo, never()).save(any());
    }

    @Test
    void create_savesScheduledTrip_withEstimatedArrival() {
        OffsetDateTime departure = OffsetDateTime.parse("2026-12-01T08:00:00+07:00");
        TripAddRequest request = new TripAddRequest(1L, 3L, departure, 180000L);

        when(vehicleRepo.findById(3L)).thenReturn(Optional.of(vehicle));
        when(routeClientAdapter.getRoute(1L)).thenReturn(activeRoute);
        when(tripRepo.save(any(Trip.class))).thenAnswer(inv -> inv.getArgument(0));

        tripService.create(request);

        verify(tripRepo).save(tripCaptor.capture());
        Trip saved = tripCaptor.getValue();
        assertEquals(departure.plusMinutes(120), saved.getEstimatedArrivalAt());
        assertEquals(TripStatus.SCHEDULED, saved.getStatus());
        assertEquals(1L, saved.getRouteId());
        assertEquals(180000L, saved.getPrice());
        assertSame(vehicle, saved.getVehicle());
    }
}