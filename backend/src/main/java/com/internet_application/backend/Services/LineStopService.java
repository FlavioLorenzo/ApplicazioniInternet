package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;

import java.util.Date;
import java.util.List;

public interface LineStopService {
    LineStopEntity getLineStopWithLineIdAndStopIdAndDir(Long lineId, Long stopId, boolean direction);
    Integer countLineStopsAfterProvidedOne(LineStopEntity lineStop);
}
