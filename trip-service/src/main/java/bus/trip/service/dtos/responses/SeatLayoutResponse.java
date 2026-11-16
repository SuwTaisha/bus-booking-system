package bus.trip.service.dtos.responses;

import bus.trip.service.models.SeatLayout;

public record SeatLayoutResponse(
        Long id,
        String seatCode,
        short deckNo,
        short rowNo,
        short colNo) {

    public static SeatLayoutResponse from(SeatLayout s) {
        return new SeatLayoutResponse(s.getId(), s.getSeatCode(), s.getDeckNo(), s.getRowNo(), s.getColNo());
    }
}
