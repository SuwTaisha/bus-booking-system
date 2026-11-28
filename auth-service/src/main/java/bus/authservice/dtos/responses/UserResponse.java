package bus.authservice.dtos.responses;

import bus.authservice.models.Role;
import bus.authservice.models.User;

public record UserResponse(Long id, String fullName, String email, String phoneNumber,
                           Role role, boolean active) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(),
                user.getPhoneNumber(), user.getRole(), user.isActive());
    }
}