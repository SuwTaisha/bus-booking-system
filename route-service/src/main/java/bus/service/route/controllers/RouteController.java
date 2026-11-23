package bus.service.route.controllers;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.RouteAddRequest;
import bus.service.route.dtos.requests.RouteEditRequest;
import bus.service.route.dtos.responses.RouteDetailResponse;
import bus.service.route.dtos.responses.RouteResponse;
import bus.service.route.exceptions.BadRequestException;
import bus.service.route.services.RouteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/batch")
    public ResponseEntity<ApiResponse<List<RouteResponse>>> getBatch(@RequestParam List<Long> ids) {
        if(ids.size() > 50) {
            throw new BadRequestException("Can recieve only 50 ids");
        }
        List<RouteResponse> result = routeService.fetchByBatch(ids);
        return ResponseEntity.ok(ApiResponse.success("Fetched complete for provided IDS", result));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<RouteResponse>>> getAll(@Valid BasePageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Fetched all routes", routeService.fetchAll(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteDetailResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Found route", routeService.fetchById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RouteDetailResponse>> create(@Valid @RequestBody RouteAddRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Created route", routeService.create(request)));
    }

    @PatchMapping("/edit")
    public ResponseEntity<ApiResponse<RouteDetailResponse>> edit(@Valid @RequestBody RouteEditRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated route", routeService.update(request)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted route", null));
    }
}
