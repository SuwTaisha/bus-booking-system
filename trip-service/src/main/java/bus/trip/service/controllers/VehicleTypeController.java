package bus.trip.service.controllers;

import bus.trip.service.dtos.ApiResponse;
import bus.trip.service.dtos.requests.BasePageRequest;
import bus.trip.service.dtos.requests.VehicleTypeAddRequest;
import bus.trip.service.dtos.requests.VehicleTypeEditRequest;
import bus.trip.service.dtos.responses.VehicleTypeDetailResponse;
import bus.trip.service.dtos.responses.VehicleTypeResponse;
import bus.trip.service.services.VehicleTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/vehicle-types")
public class VehicleTypeController {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<VehicleTypeResponse>>> getAll(@Valid BasePageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fetched all vehicle types", vehicleTypeService.fetchAll(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleTypeDetailResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found vehicle type", vehicleTypeService.fetchById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VehicleTypeDetailResponse>> create(@Valid @RequestBody VehicleTypeAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Created vehicle type", vehicleTypeService.create(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<VehicleTypeDetailResponse>> edit(@Valid @RequestBody VehicleTypeEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated vehicle type", vehicleTypeService.update(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        vehicleTypeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted vehicle type", null));
    }
}
