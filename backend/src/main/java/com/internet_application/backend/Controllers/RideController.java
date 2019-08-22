package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
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

    @PutMapping("/rides/{rideId}/close/{stopId}")
    public RideEntity putCloseStop(
            @PathVariable(value="rideId") Long rideId,
            @PathVariable(value="stopId") Long stopId) {
        return rideService.closeStop(rideId, stopId);
    }

    @PutMapping("/rides/{rideId}/close")
    public RideEntity putCloseRide(
            @PathVariable(value="rideId") Long rideId) {
        return rideService.closeRide(rideId);
    }
}
