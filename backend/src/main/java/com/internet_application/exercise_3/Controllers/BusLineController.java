package com.internet_application.exercise_3.Controllers;

import com.internet_application.exercise_3.Entities.BusLineEntity;
import com.internet_application.exercise_3.Entities.LineStopEntity;
import com.internet_application.exercise_3.Entities.StopEntity;
import com.internet_application.exercise_3.Repositories.LineStopRepository;
import com.internet_application.exercise_3.Repositories.StopRepository;
import com.internet_application.exercise_3.Services.BusLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public List<List<LineStopEntity>> getBusLineWithName(@PathVariable(value="line_id") Long lineId)
        throws ResponseStatusException{
        return busLineService.getBusLineWithName(lineId);
    }

    /* Tests */
    @GetMapping("/stops")
    public List<StopEntity> getAllBusStops() {
        return stopRepository.findAll();
    }

    @GetMapping("/linestops")
    public List<LineStopEntity> getAllLineStops() { return lineStopRepository.findAll(); }
}
