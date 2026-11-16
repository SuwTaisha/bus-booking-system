package bus.trip.service.repositories;

import bus.trip.service.models.SeatLayout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatLayoutRepo extends JpaRepository<SeatLayout, Long> {

    List<SeatLayout> findByVehicleTypeIdOrderByDeckNoAscRowNoAscColNoAsc(Long vehicleTypeId);

    boolean existsByVehicleTypeIdAndSeatCode(Long vehicleTypeId, String seatCode);

    boolean existsByVehicleTypeIdAndSeatCodeAndIdNot(Long vehicleTypeId, String seatCode, Long excludeId);

    int countByVehicleTypeId(Long vehicleTypeId);
}
