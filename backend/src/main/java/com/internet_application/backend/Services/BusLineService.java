package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;

import java.util.List;

public interface BusLineService {
    List<BusLineEntity> getAllBusLines();

    List<List<LineStopEntity>> getBusLineWithName(Long lineId);
}
