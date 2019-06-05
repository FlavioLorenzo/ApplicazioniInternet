package com.internet_application.exercise_3.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.exercise_3.Entities.ReservationEntity;
import com.internet_application.exercise_3.Services.ReservationService;
import com.internet_application.exercise_3.PostBodies.ReservationPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations/{line_id}/{date}")
    public JsonNode getAllReservationForLineAndData(@PathVariable(value="line_id") Long lineId,
                                                    @PathVariable(value="date") String date)
            throws ResponseStatusException {
        return reservationService.getAllReservationForLineAndData(lineId, date);
    }

    @PutMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public ReservationEntity putReservation(@PathVariable(value="line_id") Long lineId,
                                            @PathVariable(value="date") String date,
                                            @PathVariable(value="reservation_id") Long reservationId,
                                            @RequestBody ReservationPostBody rpb)
            throws ResponseStatusException {
        return reservationService.updateReservation(lineId, date, reservationId, rpb);
    }

    // TODO Aggiungere controllo prenotazione gia` esistente
    @PostMapping("/reservations/{line_id}/{date}")
    public ReservationEntity postReservation(@PathVariable(value="line_id") Long lineId,
                                       @PathVariable(value="date") String date,
                                       @RequestBody ReservationPostBody rpb)
            throws ResponseStatusException {
        return reservationService.addReservation(lineId, date, rpb);
    }

    @DeleteMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public void deleteReservation(@PathVariable(value="line_id") Long lineId,
                                         @PathVariable(value="date") String date,
                                         @PathVariable(value="reservation_id") Long res_id)
            throws ResponseStatusException {
        reservationService.deleteReservation(lineId, date, res_id);
    }


    @GetMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public ReservationEntity getReservation(@PathVariable(value="line_id") Long lineId,
                                      @PathVariable(value="date") String date,
                                      @PathVariable(value="reservation_id") Long res_id)
            throws ResponseStatusException {
        ReservationEntity reservation = reservationService.getReservation(lineId, date, res_id);
        return reservation;
    }
}
