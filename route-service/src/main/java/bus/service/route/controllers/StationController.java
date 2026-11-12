package bus.service.route.controllers;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.StationAddRequest;
import bus.service.route.dtos.requests.StationDetailRequest;
import bus.service.route.dtos.requests.StationEditRequest;
import bus.service.route.dtos.responses.StationDetailResponse;
import bus.service.route.dtos.responses.StationResponse;
import bus.service.route.models.Station;
import bus.service.route.services.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<StationResponse>>> getAllStations(@Valid BasePageRequest request) {
        Page<StationResponse> result = stationService.fetchAllStation(request);
        return ResponseEntity.ok(ApiResponse.success("Fetched all stations", result));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StationDetailResponse>> getStationDetail(@Valid StationDetailRequest request) {
        StationDetailResponse result = stationService.fetchStationById(request);
        return ResponseEntity.ok(ApiResponse.success("Found matched station!", result));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<StationDetailResponse>> addNewStation(@Valid @RequestBody StationAddRequest request) {
        StationDetailResponse result = stationService.createNewStation(request);
        return ResponseEntity.ok(ApiResponse.success("Created new station!", result));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<StationDetailResponse>> updateStation(@Valid @RequestBody StationEditRequest request) {
        StationDetailResponse result = stationService.updateStation(request);
        return ResponseEntity.ok(ApiResponse.success("Created new station!", result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStation(@Valid @PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted station!", null));
    }
}
