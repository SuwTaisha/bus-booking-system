package bus.authservice.controllers;

import bus.authservice.dtos.ApiResponse;
import bus.authservice.dtos.requests.RegisterRequest;
import bus.authservice.dtos.responses.UserResponse;
import bus.authservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return ResponseEntity.ok(ApiResponse.success("Current user", userService.getById(userId)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse result = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", result));
    }

}
