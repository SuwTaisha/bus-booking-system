package bus.service.route.controllers;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.models.Station;
import bus.service.route.services.StationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/route")
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/stations")
    public ResponseEntity<ApiResponse<Page<Station>>> getAllStations(@Valid @RequestParam(defaultValue = "0") int page, @Valid @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Station> result = null;
        return ResponseEntity.ok(ApiResponse.success("Fetched all stations", result));
    }
}
