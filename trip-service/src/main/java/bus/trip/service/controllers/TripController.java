package bus.trip.service.controllers;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.requests.*;
import bus.trip.service.dtos.responses.TripResponse;
import bus.trip.service.services.TripService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<TripResponse>>> getAllTrip(@Valid BasePageRequest request) {
        Page<TripResponse> resposne = tripService.getAll(request);
        return ResponseEntity.ok(ApiResponse.success("Featched Trips", resposne));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TripResponse>>> search(@Valid TripSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fetched trips", tripService.search(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found trip", tripService.fetchById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TripResponse>> create(@Valid @RequestBody TripAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Created trip", tripService.create(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<TripResponse>> edit(@Valid @RequestBody TripEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated trip", tripService.update(request.routeId(), request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TripResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody TripStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", tripService.updateStatus(id, request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted trip", null));
    }
}
