package bus.trip.service.services;

import bus.trip.service.dtos.requests.SeatLayoutAddRequest;
import bus.trip.service.dtos.requests.SeatLayoutEditRequest;
import bus.trip.service.dtos.responses.SeatLayoutResponse;
import bus.trip.service.dtos.responses.TripSeatMapResponse;
import bus.trip.service.exceptions.ConflictException;
import bus.trip.service.exceptions.ResourceNotFoundException;
import bus.trip.service.models.SeatLayout;
import bus.trip.service.models.Trip;
import bus.trip.service.models.Vehicle;
import bus.trip.service.models.VehicleType;
import bus.trip.service.repositories.SeatLayoutRepo;
import bus.trip.service.repositories.TripRepo;
import bus.trip.service.repositories.VehicleRepo;
import bus.trip.service.repositories.VehicleTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SeatLayoutService {

    @Autowired
    private SeatLayoutRepo seatLayoutRepo;

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private VehicleTypeRepo vehicleTypeRepo;

    @Autowired
    private TripRepo tripRepo;

    @Transactional(readOnly = true)
    public TripSeatMapResponse fetchByTrip(Long tripId) {
        Trip trip = tripRepo.findById(tripId).orElseThrow(() -> new ResourceNotFoundException("Cannot find trip with this Id"));
        VehicleType vehicleType = trip.getVehicle().getVehicleType();
        List<SeatLayoutResponse> seatLayout = seatLayoutRepo
                .findByVehicleTypeIdOrderByDeckNoAscRowNoAscColNoAsc(vehicleType.getId())
                .stream().map(SeatLayoutResponse::from).toList();

        TripSeatMapResponse response = TripSeatMapResponse.from(trip, seatLayout);
        return response;
    }


    @Transactional(readOnly = true)
    public List<SeatLayoutResponse> fetchByVehicleType(Long vehicleTypeId) {
        findVehicleTypeOrThrow(vehicleTypeId);
        return seatLayoutRepo
                .findByVehicleTypeIdOrderByDeckNoAscRowNoAscColNoAsc(vehicleTypeId)
                .stream().map(SeatLayoutResponse::from).toList();
    }

    @Transactional
    public SeatLayoutResponse add(SeatLayoutAddRequest request) {
        VehicleType vt = findVehicleTypeOrThrow(request.vehicleTypeId());
        String code = request.seatCode().trim().toUpperCase();

        if (seatLayoutRepo.existsByVehicleTypeIdAndSeatCode(vt.getId(), code)) {
            throw new ConflictException("Seat code '" + code + "' already exists for this vehicle type!");
        }

        SeatLayout seat = new SeatLayout();
        seat.setVehicleType(vt);
        seat.setSeatCode(code);
        seat.setDeckNo(request.deckNo());
        seat.setRowNo(request.rowNo());
        seat.setColNo(request.colNo());
        return SeatLayoutResponse.from(seatLayoutRepo.save(seat));
    }

    @Transactional
    public SeatLayoutResponse edit(SeatLayoutEditRequest request) {
        SeatLayout seat = seatLayoutRepo.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException("Seat layout not found: " + request.id()));

        if (StringUtils.hasText(request.seatCode())) {
            String code = request.seatCode().trim().toUpperCase();
            if (seatLayoutRepo.existsByVehicleTypeIdAndSeatCodeAndIdNot(seat.getVehicleType().getId(), code, seat.getId())) {
                throw new ConflictException("Seat code '" + code + "' already exists for this vehicle type!");
            }
            seat.setSeatCode(code);
        }
        if (request.deckNo() != null) seat.setDeckNo(request.deckNo());
        if (request.rowNo() != null) seat.setRowNo(request.rowNo());
        if (request.colNo() != null) seat.setColNo(request.colNo());
        return SeatLayoutResponse.from(seat);
    }

    @Transactional
    public void remove(Long id) {
        SeatLayout seat = seatLayoutRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat layout not found: " + id));
        seatLayoutRepo.delete(seat);
    }

    private VehicleType findVehicleTypeOrThrow(Long id) {
        return vehicleTypeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle type not found: " + id));
    }
}
