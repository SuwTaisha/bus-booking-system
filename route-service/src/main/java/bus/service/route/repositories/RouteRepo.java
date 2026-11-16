package bus.service.route.repositories;

import bus.service.route.models.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRepo extends JpaRepository<Route, Long> {

    Page<Route> findByActiveTrue(Pageable pageable);

    /** Check for active route uniqueness: (startStation, endStation, line) */
    boolean existsByStartStationIdAndEndStationIdAndLineAndActiveTrue(
            Long startStationId, Long endStationId, String line);

    /** Exclude self when checking for duplicates on edit */
    @Query("""
            SELECT COUNT(r) > 0 FROM Route r
            WHERE r.startStation.id = :startId
              AND r.endStation.id   = :endId
              AND r.line            = :line
              AND r.active          = true
              AND r.id             <> :excludeId
            """)
    boolean existsActiveDuplicateExcluding(
            @Param("startId") Long startId,
            @Param("endId") Long endId,
            @Param("line") String line,
            @Param("excludeId") Long excludeId);
}
