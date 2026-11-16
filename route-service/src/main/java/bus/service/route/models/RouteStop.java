package bus.service.route.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "route_stops")
@Getter
@Setter
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rest_stop_id", nullable = false)
    private RestStop restStop;

    /** Stop sequence on the route, starting from 1 */
    @Column(nullable = false)
    private short stopOrder;

    /** Number of minutes to rest at this stop */
    @Column(nullable = false)
    private short restMinutes;
}
