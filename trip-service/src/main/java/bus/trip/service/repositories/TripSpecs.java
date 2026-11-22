package bus.trip.service.repositories;

import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class TripSpecs {

    public static Specification<Trip> hasRoute(Long routeId) {
        return (root, query, cb) -> cb.equal(root.get("routeId"), routeId);
    }

    public static Specification<Trip> hasStatus(TripStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Trip> departsBetween(OffsetDateTime from, OffsetDateTime to) {
        return (root, query, cb) -> cb.and(
                cb.greaterThanOrEqualTo(root.get("departureAt"), from),
                cb.lessThan(root.get("departureAt"), to));
    }

    public static Specification<Trip> departsAfter(OffsetDateTime time) {
        return (root, query, cb) -> cb.greaterThan(root.get("departureAt"), time);
    }

    public static Specification<Trip> priceAtLeast(Long min) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Long>get("price"), min);
    }

    public static Specification<Trip> priceAtMost(Long max) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.<Long>get("price"), max);
    }
}
