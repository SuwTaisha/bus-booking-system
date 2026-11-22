package bus.trip.service.dtos.requests;

import bus.trip.service.models.TripStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public record TripSearchRequest(
        Long routeId,
        LocalDate date,
        TripStatus status,
        @PositiveOrZero Long minPrice,
        @PositiveOrZero Long maxPrice,
        String sortBy,
        String direction,
        @Min(0) Integer page,
        @Min(1) @Max(30) Integer size) {

    public TripSearchRequest {
        if (page == null) page = 0;
        if (size == null) size = 10;
    }

    public Pageable toPageable() {
        String field = "price".equals(sortBy) ? "price" : "departureAt";
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(dir, field));
    }
}