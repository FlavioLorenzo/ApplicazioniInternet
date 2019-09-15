package com.internet_application.backend.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.ChildService;
import com.internet_application.backend.Services.PrincipalService;
import com.internet_application.backend.Services.ReservationService;
import com.internet_application.backend.PostBodies.ReservationPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin()
@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private BusLineService busLineService;
    @Autowired
    private ChildService childService;
    @Autowired
    private PrincipalService principalService;

    // no parent
    @GetMapping("/reservations/{line_id}/{date}")
    public JsonNode getAllReservationForLineAndData(@PathVariable(value="line_id") Long lineId,
                                                    @PathVariable(value="date") String date)
            throws ResponseStatusException {
        return reservationService.getAllReservationForLineAndData(lineId, date);
    }

    @PutMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public ReservationEntity putReservation(Principal principal,
                                            @PathVariable(value="line_id") Long lineId,
                                            @PathVariable(value="date") String date,
                                            @PathVariable(value="reservation_id") Long reservationId,
                                            @RequestBody ReservationPostBody rpb)
            throws ResponseStatusException {
        /* Security check */
        if(!principalService.canUserEditReservation(principal, reservationId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if(principalService.IsUserEscortInRide(principal,
                reservationService.getReservation(lineId, date, reservationId).getRide().getId())) {
            return reservationService.updateReservation(lineId, date, reservationId, rpb, true);
        } else {
            return reservationService.updateReservation(lineId, date, reservationId, rpb, false);
        }
    }


    @PostMapping("/reservations/{line_id}/{date}")
    public ReservationEntity postReservation(Principal principal,
                                       @PathVariable(value="line_id") Long lineId,
                                       @PathVariable(value="date") String date,
                                       @RequestBody ReservationPostBody rpb)
            throws ResponseStatusException {
        /* Security check */
        /* Parent of child */
        if (principalService.IsUserParent(principal) &&
            !principalService.IsUserParentOfChild(principal, rpb.id_child))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return reservationService.addReservation(lineId, date, rpb);
    }

    @DeleteMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public void deleteReservation(Principal principal,
                                         @PathVariable(value="line_id") Long lineId,
                                         @PathVariable(value="date") String date,
                                         @PathVariable(value="reservation_id") Long res_id)
            throws ResponseStatusException {
        /* Security check */
        if(!principalService.canUserEditReservation(principal, res_id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        reservationService.deleteReservation(lineId, date, res_id);
    }

    @GetMapping("/reservations/{line_id}/{date}/{reservation_id}")
    public ReservationEntity getReservation(Principal principal,
                                      @PathVariable(value="line_id") Long lineId,
                                      @PathVariable(value="date") String date,
                                      @PathVariable(value="reservation_id") Long res_id)
            throws ResponseStatusException {
        ReservationEntity reservation = reservationService.getReservation(lineId, date, res_id);
        /* Security check */
        /* Parent of child */
        if (principalService.IsUserParent(principal) &&
                !principalService.IsUserParentOfChild(principal, reservation.getChild().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return reservation;
    }

    @GetMapping("/reservations/{line_id}/{child_id}/{date}/{n}")
    public List<ReservationEntity> getNReservationsByUserFromDate(Principal principal,
                                            @PathVariable(value="line_id") Long lineId,
                                            @PathVariable(value="child_id") Long child_id,
                                            @PathVariable(value="date") String date,
                                            @PathVariable(value="n") Integer n)
            throws ResponseStatusException {
        /* Security check */
        /* Parent of child */
        if (principalService.IsUserParent(principal) &&
                !principalService.IsUserParentOfChild(principal, child_id))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        List<ReservationEntity> reservations =
                reservationService.getNReservationsByChildFromDate(lineId, child_id, date, n);
        return reservations;
    }
}
