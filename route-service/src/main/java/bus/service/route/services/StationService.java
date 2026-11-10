package bus.service.route.services;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.responses.StationResponse;
import bus.service.route.repositories.StationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationRepo stationRepo;

    public Page<StationResponse> getAll(Pageable pageable) {
        return stationRepo.findAll(pageable).stream().map();
    }
}
