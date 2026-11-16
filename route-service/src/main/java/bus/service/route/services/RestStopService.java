package bus.service.route.services;

import bus.service.route.dtos.requests.BasePageRequest;
import bus.service.route.dtos.requests.RestStopAddRequest;
import bus.service.route.dtos.requests.RestStopEditRequest;
import bus.service.route.dtos.responses.RestStopResponse;
import bus.service.route.exceptions.ResourceNotFoundException;
import bus.service.route.models.RestStop;
import bus.service.route.repositories.RestStopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for rest stops: fetch, create, update, delete.
 */
@Service
public class RestStopService {

    @Autowired
    private RestStopRepo restStopRepo;

    @Transactional(readOnly = true)
    public Page<RestStopResponse> fetchAll(BasePageRequest request) {
        Pageable pageable = request.toPageable(Sort.by("name"));
        return restStopRepo.findByActiveTrue(pageable).map(RestStopResponse::from);
    }

    @Transactional(readOnly = true)
    public RestStopResponse fetchById(Long id) {
        RestStop restStop = restStopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rest stop not found: " + id));
        return RestStopResponse.from(restStop);
    }

    @Transactional
    public RestStopResponse create(RestStopAddRequest request) {
        return RestStopResponse.from(restStopRepo.save(request.toEntity()));
    }

    @Transactional
    public RestStopResponse update(RestStopEditRequest request) {
        RestStop restStop = restStopRepo.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Rest stop not found: " + request.id()));
        request.applyTo(restStop);
        return RestStopResponse.from(restStop);
    }

    @Transactional
    public void delete(Long id) {
        RestStop restStop = restStopRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rest stop not found: " + id));
        restStopRepo.delete(restStop);
    }
}
