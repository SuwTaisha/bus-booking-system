package bus.trip.service.controllers;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.requests.BasePageRequest;
import bus.trip.service.dtos.requests.VehicleAddRequest;
import bus.trip.service.dtos.requests.VehicleEditRequest;
import bus.trip.service.dtos.responses.VehicleResponse;
import bus.trip.service.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<VehicleResponse>>> getAll(@Valid BasePageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fetched all vehicles", vehicleService.fetchAll(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found vehicle", vehicleService.fetchById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleResponse>> create(@Valid @RequestBody VehicleAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Created vehicle", vehicleService.create(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<VehicleResponse>> edit(@Valid @RequestBody VehicleEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated vehicle", vehicleService.update(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted vehicle", null));
    }
}
