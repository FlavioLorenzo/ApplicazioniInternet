package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface LineStopService {
    LineStopEntity getLineStopWithLineIdAndStopIdAndDir(Long lineId, Long stopId, boolean direction);
    List<LineStopEntity> getLineStopsAvailableWithLineIdAndDir(Long lineId, boolean dir, Long lastStopId);
    List<LineStopEntity> getLineStopsWithLineIdAndDir(Long lineId, boolean dir);
    LineStopEntity getFirstLineStopAvailableWithLineIdAndDir(Long lineId, boolean dir, Long lastStopId);
    LineStopEntity getFirstLineStopWithLineIdAndDir(Long lineId, boolean dir);
    Integer countLineStopsAfterProvidedOne(LineStopEntity lineStop);
}
