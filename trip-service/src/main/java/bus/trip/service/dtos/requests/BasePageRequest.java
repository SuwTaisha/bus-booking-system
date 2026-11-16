package bus.trip.service.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record BasePageRequest(
        @Min(0) Integer page,
        @Min(1) @Max(value = 30, message = "Page size cannot be more than 30!") Integer size) {

    public BasePageRequest {
        if (page == null) page = 0;
        if (size == null) size = 10;
    }

    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
