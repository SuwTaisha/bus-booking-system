package bus.trip.service.controllers;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.requests.SeatLayoutAddRequest;
import bus.trip.service.dtos.requests.SeatLayoutEditRequest;
import bus.trip.service.dtos.responses.SeatLayoutResponse;
import bus.trip.service.services.SeatLayoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip/seat-layouts")
public class SeatLayoutController {

    @Autowired
    private SeatLayoutService seatLayoutService;

    @GetMapping("/by-vehicle-type/{vehicleTypeId}")
    public ResponseEntity<ApiResponse<List<SeatLayoutResponse>>> getByVehicleType(@PathVariable Long vehicleTypeId) {
        return ResponseEntity.ok(ApiResponse.success("Fetched seat layouts", seatLayoutService.fetchByVehicleType(vehicleTypeId)));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<SeatLayoutResponse>> add(@Valid @RequestBody SeatLayoutAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Added seat", seatLayoutService.add(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<SeatLayoutResponse>> edit(@Valid @RequestBody SeatLayoutEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated seat", seatLayoutService.edit(request)));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        seatLayoutService.remove(id);
        return ResponseEntity.ok(ApiResponse.success("Removed seat", null));
    }
}
