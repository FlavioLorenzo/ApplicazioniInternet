package com.internet_application.backend.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.PostBodies.ChangeLockedStatusPostBody;
import com.internet_application.backend.PostBodies.ReservationPostBody;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Repositories.StopRepository;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.PrincipalService;
import com.internet_application.backend.Services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/*
* SECURITY POLICY
* Everybody can GET
* Only system administrator and owners can modify
* */

@CrossOrigin()
@RestController
public class RideController {
    @Autowired
    private RideService rideService;
    @Autowired
    private PrincipalService principalService;

    @GetMapping("/rides/{lineId}/{date}/{n}")
    public List<RideEntity> getNRidesFromDate(
            @PathVariable(value="lineId") Long lineId,
            @PathVariable(value="date") String date,
            @PathVariable(value="n") Integer n) {
        return rideService.getNRidesFromDate(lineId, date, n);
    }

    @GetMapping("/rides/locked/{userId}/{date}")
    public JsonNode getLockedRidesFromUserAndDate(
            @PathVariable(value="userId") Long userId,
            @PathVariable(value="date") String date) {
        return rideService.getLockedRidesFromUserAndDate(userId, date);
    }

    @GetMapping("/rides/{rideId}/available")
    public List<LineStopEntity> getAvailableStops(@PathVariable(value="rideId") Long rideId) {
        return rideService.getAvailableStops(rideId);
    }

    @GetMapping("/rides/manage/{userId}/{rideId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public JsonNode getRideAvailabilityInfo(@PathVariable(value="userId") Long userId,
                                            @PathVariable(value="rideId") Long rideId)
            throws ResponseStatusException {
        return rideService.getRideAvailabilityInfo(userId, rideId);
    }

    @GetMapping("/rides/manage/{userId}/{fromDate}/{toDate}/{openOrLocked}")
    public JsonNode getAdministeredLinesRidesFromDateToDate(@PathVariable(value="userId") Long userId,
                                                            @PathVariable(value="fromDate") String fromDate,
                                                            @PathVariable(value="toDate") String toDate,
                                                            @PathVariable(value="openOrLocked") String openOrLocked)
            throws ResponseStatusException {
        return rideService.getAdministeredLinesRidesFromDateToDate(userId, fromDate, toDate, openOrLocked);
    }

    @PutMapping("/rides/{rideId}/close/{stopId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public RideEntity putCloseStop(
            Principal principal,
            @PathVariable(value="rideId") Long rideId,
            @PathVariable(value="stopId") Long stopId)
            throws ResponseStatusException {
        /* Security check -> assigned escort */

        if(!principalService.canUserEditRide(principal, rideId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return rideService.closeStop(rideId, stopId);
    }

    @PutMapping("/rides/{rideId}/open")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public RideEntity putOpenRide(
            Principal principal,
            @PathVariable(value="rideId") Long rideId)
            throws ResponseStatusException {
        /* Security check -> assigned escort */
        System.out.println("Reached Here!");
        if(!principalService.canUserEditRide(principal, rideId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        System.out.println("Reached after");
        return rideService.openRide(rideId);
    }

    @PutMapping("/rides/{rideId}/close")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public RideEntity putCloseRide(
            Principal principal,
            @PathVariable(value="rideId") Long rideId)
            throws ResponseStatusException {
        /* Security check -> assigned escort */
        if(!principalService.canUserEditRide(principal, rideId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return rideService.closeRide(rideId);
    }

    @PutMapping("/rides/{rideId}/lock-unlock")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN')")
    public RideEntity putLockUnlockRide(
            Principal principal,
            @PathVariable(value="rideId") Long rideId,
            @RequestBody ChangeLockedStatusPostBody clspb)
            throws ResponseStatusException {
        /* Security check -> line admin */
        RideEntity ride = rideService.getRide(rideId);
        if (principalService.IsUserAdmin(principal) &&
                !principalService.IsUserAdminOfLine(principal, ride.getLine().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return rideService.lockUnlock(rideId, clspb.getLocked());
    }
}
