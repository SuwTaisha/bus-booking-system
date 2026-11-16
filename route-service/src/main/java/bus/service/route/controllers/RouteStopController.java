package bus.service.route.controllers;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.requests.RouteStopAddRequest;
import bus.service.route.dtos.requests.RouteStopEditRequest;
import bus.service.route.dtos.responses.RouteStopResponse;
import bus.service.route.services.RouteStopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route/route-stops")
public class RouteStopController {

    @Autowired
    private RouteStopService routeStopService;

    @GetMapping("/by-route/{routeId}")
    public ResponseEntity<ApiResponse<List<RouteStopResponse>>> getByRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(ApiResponse.success("Fetched stops for route", routeStopService.fetchByRoute(routeId)));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RouteStopResponse>> add(@Valid @RequestBody RouteStopAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Added route stop", routeStopService.add(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<RouteStopResponse>> edit(@Valid @RequestBody RouteStopEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated route stop", routeStopService.edit(request)));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        routeStopService.remove(id);
        return ResponseEntity.ok(ApiResponse.success("Removed route stop", null));
    }
}
