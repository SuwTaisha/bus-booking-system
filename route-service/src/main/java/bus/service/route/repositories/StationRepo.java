package bus.service.route.repositories;

import bus.service.route.models.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepo extends JpaRepository<Station, Long> {
    Page<Station> findByActiveTrue(Pageable pageable);
}
