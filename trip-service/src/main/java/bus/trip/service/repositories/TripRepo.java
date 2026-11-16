package bus.trip.service.repositories;

import bus.trip.service.models.Trip;
import bus.trip.service.models.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface TripRepo extends JpaRepository<Trip, Long> {

    Page<Trip> findByRouteId(Long routeId, Pageable pageable);

    Page<Trip> findByStatus(TripStatus status, Pageable pageable);

    Page<Trip> findByRouteIdAndStatus(Long routeId, TripStatus status, Pageable pageable);

    /**
     * Check vehicle schedule overlap for create.
     * Excludes CANCELLED trips (mirrors the DB exclusion constraint).
     */
    @Query("""
            SELECT COUNT(t) > 0 FROM Trip t
            WHERE t.vehicle.id = :vehicleId
              AND t.status     <> 'CANCELLED'
              AND t.departureAt         < :estimatedArrivalAt
              AND t.estimatedArrivalAt  > :departureAt
            """)
    boolean hasVehicleOverlap(
            @Param("vehicleId") Long vehicleId,
            @Param("departureAt") OffsetDateTime departureAt,
            @Param("estimatedArrivalAt") OffsetDateTime estimatedArrivalAt);

    /** Same check but excludes the trip being edited */
    @Query("""
            SELECT COUNT(t) > 0 FROM Trip t
            WHERE t.vehicle.id = :vehicleId
              AND t.id         <> :excludeId
              AND t.status     <> 'CANCELLED'
              AND t.departureAt         < :estimatedArrivalAt
              AND t.estimatedArrivalAt  > :departureAt
            """)
    boolean hasVehicleOverlapExcluding(
            @Param("vehicleId") Long vehicleId,
            @Param("departureAt") OffsetDateTime departureAt,
            @Param("estimatedArrivalAt") OffsetDateTime estimatedArrivalAt,
            @Param("excludeId") Long excludeId);
}
