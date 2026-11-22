package bus.trip.service.controllers;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.requests.TripAddRequest;
import bus.trip.service.dtos.requests.TripEditRequest;
import bus.trip.service.dtos.requests.TripSearchRequest;
import bus.trip.service.dtos.requests.TripStatusRequest;
import bus.trip.service.dtos.responses.TripResponse;
import bus.trip.service.services.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/trips")
public class TripController {

    @Autowired
    private TripService tripService;

//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse<Page<TripResponse>>> search(@Valid TripSearchRequest request) {
//        return ResponseEntity.ok(ApiResponse.success("Fetched trips", tripService.search(request)));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found trip", tripService.fetchById(id)));
    }
//
//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse<TripResponse>> create(@Valid @RequestBody TripAddRequest request) {
//        return ResponseEntity.ok(ApiResponse.success("Created trip", tripService.create(request)));
//    }
//
//    @PatchMapping("/edit")
//    public ResponseEntity<ApiResponse<TripResponse>> edit(@Valid @RequestBody TripEditRequest request) {
//        return ResponseEntity.ok(ApiResponse.success("Updated trip", tripService.update(request)));
//    }
//
//    @PatchMapping("/status")
//    public ResponseEntity<ApiResponse<TripResponse>> updateStatus(@Valid @RequestBody TripStatusRequest request) {
//        return ResponseEntity.ok(ApiResponse.success("Status updated", tripService.updateStatus(request)));
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted trip", null));
    }
}
