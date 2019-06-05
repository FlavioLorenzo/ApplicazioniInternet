package com.internet_application.exercise_3.Services;

import com.internet_application.exercise_3.Entities.BusLineEntity;
import com.internet_application.exercise_3.Entities.LineStopEntity;

import java.util.List;

public interface BusLineService {
    List<BusLineEntity> getAllBusLines();

    List<List<LineStopEntity>> getBusLineWithName(Long lineId);
}
