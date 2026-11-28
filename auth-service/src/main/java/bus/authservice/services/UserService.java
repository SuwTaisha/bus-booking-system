package bus.authservice.services;

import java.util.Locale;

import bus.authservice.dtos.requests.RegisterRequest;
import bus.authservice.dtos.responses.UserResponse;
import bus.authservice.exceptions.ConflictException;
import bus.authservice.exceptions.ResourceNotFoundException;
import bus.authservice.models.Role;
import bus.authservice.models.User;
import bus.authservice.repositories.UserRepo;
import jakarta.ws.rs.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailTaken(String email) {
        return userRepo.existsByEmailIgnoreCase(normalizeEmail(email));
    }

    public boolean isPhoneTaken(String phoneNumber) {
        return userRepo.existsByPhoneNumber(normalizePhone(phoneNumber));
    }

    public UserResponse getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.isActive()) {
            throw new ForbiddenException("Account is disabled");
        }
        return UserResponse.from(user);
    }

    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        String phone = normalizePhone(request.phoneNumber());

        boolean emailTaken = userRepo.existsByEmailIgnoreCase(email);
        boolean phoneTaken = userRepo.existsByPhoneNumber(phone);

        if (emailTaken && phoneTaken) {
            throw new ConflictException("Email and phone number are already taken");
        }
        if (emailTaken) {
            throw new ConflictException("Email is already taken");
        }
        if (phoneTaken) {
            throw new ConflictException("Phone number is already taken");
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(request.fullName().trim());
        user.setPhoneNumber(phone);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);

        return UserResponse.from(userRepo.save(user));
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizePhone(String phoneNumber) {
        String phone = phoneNumber.trim();
        return phone.startsWith("+84") ? "0" + phone.substring(3) : phone;
    }
}