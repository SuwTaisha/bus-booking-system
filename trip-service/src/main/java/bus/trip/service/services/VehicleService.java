package bus.trip.service.services;

import bus.trip.service.dtos.requests.BasePageRequest;
import bus.trip.service.dtos.requests.VehicleAddRequest;
import bus.trip.service.dtos.requests.VehicleEditRequest;
import bus.trip.service.dtos.responses.VehicleResponse;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.Vehicle;
import bus.trip.service.models.VehicleType;
import bus.trip.service.repositories.VehicleRepo;
import bus.trip.service.repositories.VehicleTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private VehicleTypeRepo vehicleTypeRepo;

    @Transactional(readOnly = true)
    public Page<VehicleResponse> fetchAll(BasePageRequest request) {
        return vehicleRepo.findByActiveTrue(request.toPageable(Sort.by("licensePlate")))
                .map(VehicleResponse::from);
    }

    @Transactional(readOnly = true)
    public VehicleResponse fetchById(Long id) {
        return VehicleResponse.from(findOrThrow(id));
    }

    @Transactional
    public VehicleResponse create(VehicleAddRequest request) {
        String plate = request.licensePlate().trim().toUpperCase();
        if (vehicleRepo.existsByLicensePlate(plate)) {
            throw new ConflictException("License plate already exists: " + plate);
        }
        VehicleType vt = findVehicleTypeOrThrow(request.vehicleTypeId());

        Vehicle v = new Vehicle();
        v.setVehicleType(vt);
        v.setLicensePlate(plate);
        v.setModel(request.model());
        v.setManufactureYear(request.manufactureYear());
        v.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) v.setInactiveReason(request.inactiveReason().trim());
        return VehicleResponse.from(vehicleRepo.save(v));
    }

    @Transactional
    public VehicleResponse update(VehicleEditRequest request) {
        Vehicle v = findOrThrow(request.id());

        if (request.vehicleTypeId() != null) {
            v.setVehicleType(findVehicleTypeOrThrow(request.vehicleTypeId()));
        }
        if (StringUtils.hasText(request.licensePlate())) {
            String plate = request.licensePlate().trim().toUpperCase();
            if (vehicleRepo.existsByLicensePlateAndIdNot(plate, v.getId())) {
                throw new ConflictException("License plate already exists: " + plate);
            }
            v.setLicensePlate(plate);
        }
        if (request.model() != null) v.setModel(request.model());
        if (request.manufactureYear() != null) v.setManufactureYear(request.manufactureYear());
        if (request.isActive() != null) v.setActive(request.isActive());
        if (StringUtils.hasText(request.inactiveReason())) v.setInactiveReason(request.inactiveReason().trim());
        return VehicleResponse.from(v);
    }

    @Transactional
    public void delete(Long id) {
        vehicleRepo.delete(findOrThrow(id));
    }

    private Vehicle findOrThrow(Long id) {
        return vehicleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
    }

    private VehicleType findVehicleTypeOrThrow(Long id) {
        return vehicleTypeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found: " + id));
    }
}
