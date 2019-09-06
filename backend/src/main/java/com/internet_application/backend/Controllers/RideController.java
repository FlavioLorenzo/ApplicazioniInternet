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
import com.internet_application.backend.Services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin()
@RestController
public class RideController {
    @Autowired
    private RideService rideService;

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

    // no parent
    @GetMapping("/rides/manage/{userId}/{rideId}")
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

    // scorta assegnata
    @PutMapping("/rides/{rideId}/close/{stopId}")
    public RideEntity putCloseStop(
            @PathVariable(value="rideId") Long rideId,
            @PathVariable(value="stopId") Long stopId) {
        return rideService.closeStop(rideId, stopId);
    }

    // scorta assegnata
    @PutMapping("/rides/{rideId}/open")
    public RideEntity putOpenRide(
            @PathVariable(value="rideId") Long rideId) {
        return rideService.openRide(rideId);
    }

    // scorta assegnata
    @PutMapping("/rides/{rideId}/close")
    public RideEntity putCloseRide(
            @PathVariable(value="rideId") Long rideId) {
        return rideService.closeRide(rideId);
    }

    // admin della linea
    @PutMapping("/rides/{rideId}/lock-unlock")
    public RideEntity putLockUnlockRide(
            @PathVariable(value="rideId") Long rideId,
            @RequestBody ChangeLockedStatusPostBody clspb) {
        return rideService.lockUnlock(rideId, clspb.getLocked());
    }
}
