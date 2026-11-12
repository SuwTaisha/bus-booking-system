package bus.service.route.services;

import bus.service.route.dtos.ApiResponse;
import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.StationAddRequest;
import bus.service.route.dtos.requests.StationDetailRequest;
import bus.service.route.dtos.requests.StationEditRequest;
import bus.service.route.dtos.responses.StationDetailResponse;
import bus.service.route.dtos.responses.StationResponse;
import bus.service.route.exceptions.ResourceNotFoundException;
import bus.service.route.models.Station;
import bus.service.route.repositories.StationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service class for stations, responsible for fetch, add, update, delete a station
 * Current features: Fetch all, add new, update
 */
@Service
public class StationService {

    @Autowired
    private StationRepo stationRepo;

    @Transactional(readOnly = true)
    public Page<StationResponse> fetchAllStation(BasePageRequest request) {
        Pageable pageable = request.toPageable(Sort.by("name"));
        return stationRepo.findByActiveTrue(pageable).map(StationResponse::fromStation);
    }

    @Transactional(readOnly = true)
    public StationDetailResponse fetchStationById(StationDetailRequest request) {
        Station station = stationRepo.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException("Cannot found any matched station!"));
        return StationDetailResponse.fromStation(station);
    }

    @Transactional
    public StationDetailResponse createNewStation(StationAddRequest request) {
        return StationDetailResponse.fromStation(stationRepo.save(request.toEntity()));
    }

    @Transactional
    public StationDetailResponse updateStation(StationEditRequest request) {
        Station station = stationRepo.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException("Station not found: " + request.id()));
        request.applyTo(station);
        return StationDetailResponse.fromStation(station);
    }

    @Transactional
    public void deleteStation(Long id) {
        stationRepo.delete(stationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station not found: " + id)));
    }

}
