package bus.trip.service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seat_layouts")
@Getter
@Setter
public class SeatLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_type_id", nullable = false)
    private VehicleType vehicleType;

    /** Unique seat code within a vehicle type, e.g. A1, B2 */
    @Column(nullable = false)
    private String seatCode;

    /** Deck number, starting from 1 */
    @Column(nullable = false)
    private short deckNo;

    /** Row position within the deck */
    @Column(nullable = false)
    private short rowNo;

    /** Column position within the deck */
    @Column(nullable = false)
    private short colNo;
}
