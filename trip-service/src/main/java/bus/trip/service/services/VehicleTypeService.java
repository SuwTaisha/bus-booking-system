package bus.trip.service.services;

import bus.trip.service.dtos.requests.BasePageRequest;
import bus.trip.service.dtos.requests.VehicleTypeAddRequest;
import bus.trip.service.dtos.requests.VehicleTypeEditRequest;
import bus.trip.service.dtos.responses.VehicleTypeDetailResponse;
import bus.trip.service.dtos.responses.VehicleTypeResponse;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.VehicleType;
import bus.trip.service.repositories.VehicleTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepo vehicleTypeRepo;

    @Transactional(readOnly = true)
    public Page<VehicleTypeResponse> fetchAll(BasePageRequest request) {
        return vehicleTypeRepo.findByActiveTrue(request.toPageable(Sort.by("typeName")))
                .map(VehicleTypeResponse::from);
    }

    @Transactional(readOnly = true)
    public VehicleTypeDetailResponse fetchById(Long id) {
        return VehicleTypeDetailResponse.from(findOrThrow(id));
    }

    @Transactional
    public VehicleTypeDetailResponse create(VehicleTypeAddRequest request) {
        if (vehicleTypeRepo.existsByTypeName(request.typeName().trim())) {
            throw new ConflictException("Vehicle type name already exists: " + request.typeName());
        }
        VehicleType vt = new VehicleType();
        vt.setTypeName(request.typeName().trim());
        vt.setSeatCount(request.seatCount());
        vt.setDescription(request.description());
        vt.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) vt.setInactiveReason(request.inactiveReason().trim());
        return VehicleTypeDetailResponse.from(vehicleTypeRepo.save(vt));
    }

    @Transactional
    public VehicleTypeDetailResponse update(VehicleTypeEditRequest request) {
        VehicleType vt = findOrThrow(request.id());
        if (StringUtils.hasText(request.typeName())) {
            String name = request.typeName().trim();
            if (vehicleTypeRepo.existsByTypeNameAndIdNot(name, vt.getId())) {
                throw new ConflictException("Vehicle type name already exists: " + name);
            }
            vt.setTypeName(name);
        }
        if (request.seatCount() != null) vt.setSeatCount(request.seatCount());
        if (request.description() != null) vt.setDescription(request.description());
        if (request.isActive() != null) vt.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) vt.setInactiveReason(request.inactiveReason().trim());
        return VehicleTypeDetailResponse.from(vt);
    }

    @Transactional
    public void delete(Long id) {
        vehicleTypeRepo.delete(findOrThrow(id));
    }

    private VehicleType findOrThrow(Long id) {
        return vehicleTypeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found: " + id));
    }
}
