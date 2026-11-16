package bus.service.route.controllers;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.RestStopAddRequest;
import bus.service.route.dtos.requests.RestStopEditRequest;
import bus.service.route.dtos.responses.RestStopResponse;
import bus.service.route.services.RestStopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route/rest-stops")
public class RestStopController {

    @Autowired
    private RestStopService restStopService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<RestStopResponse>>> getAll(@Valid BasePageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fetched all rest stops", restStopService.fetchAll(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestStopResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found rest stop", restStopService.fetchById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RestStopResponse>> create(@Valid @RequestBody RestStopAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Created rest stop", restStopService.create(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<RestStopResponse>> edit(@Valid @RequestBody RestStopEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated rest stop", restStopService.update(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        restStopService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted rest stop", null));
    }
}
