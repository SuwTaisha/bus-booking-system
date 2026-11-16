package bus.service.route.repositories;

import bus.service.route.models.RestStop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestStopRepo extends JpaRepository<RestStop, Long> {
    Page<RestStop> findByActiveTrue(Pageable pageable);
}
