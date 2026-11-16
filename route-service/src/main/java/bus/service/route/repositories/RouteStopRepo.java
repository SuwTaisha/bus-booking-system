package bus.service.route.repositories;

import bus.service.route.models.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStopRepo extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);

    boolean existsByRouteIdAndStopOrder(Long routeId, short stopOrder);

    boolean existsByRouteIdAndRestStopId(Long routeId, Long restStopId);

    /** Exclude self when checking duplicate stop order on edit */
    boolean existsByRouteIdAndStopOrderAndIdNot(Long routeId, short stopOrder, Long excludeId);

    /** Exclude self when checking duplicate rest stop on edit */
    boolean existsByRouteIdAndRestStopIdAndIdNot(Long routeId, Long restStopId, Long excludeId);
}
