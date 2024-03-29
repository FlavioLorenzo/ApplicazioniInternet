package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Repositories.StopRepository;
import com.internet_application.backend.Services.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
 * SECURITY POLICY
 * All methods allowed to every role
 * */

@CrossOrigin()
@RestController
public class BusLineController {
    @Autowired
    private BusLineService busLineService;
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LineStopRepository lineStopRepository;


    @GetMapping("/lines")
    public List<BusLineEntity> getAllBusLines() {
        return busLineService.getAllBusLines();
    }

    @GetMapping("/lines/{line_id}")
    public BusLineEntity getBusLineWithName(@PathVariable(value="line_id") Long lineId)
        throws ResponseStatusException{
        return busLineService.getBuslineById(lineId);
    }

    /* Tests */
    @GetMapping("/stops")
    public List<StopEntity> getAllBusStops() {
        return stopRepository.findAll();
    }

    @GetMapping("/linestops")
    public List<LineStopEntity> getAllLineStops() { return lineStopRepository.findAll(); }
}
