package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Repositories.StopRepository;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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
}
