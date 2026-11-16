package bus.trip.service.repositories;

import bus.trip.service.models.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepo extends JpaRepository<VehicleType, Long> {
    Page<VehicleType> findByActiveTrue(Pageable pageable);
    boolean existsByTypeName(String typeName);
    boolean existsByTypeNameAndIdNot(String typeName, Long excludeId);
}
