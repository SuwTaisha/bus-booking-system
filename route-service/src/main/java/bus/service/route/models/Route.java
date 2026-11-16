package bus.service.route.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
@Getter
@Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The road/highway the route travels (e.g. expressway, national road) */
    @Column(nullable = false)
    private String line;

    /** Estimated travel duration in minutes */
    @Column(nullable = false)
    private int estimatedMinutes;

    /** Distance in kilometers */
    @Column(nullable = false)
    private float distance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    private String inactiveReason;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stopOrder ASC")
    private List<RouteStop> stops = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
