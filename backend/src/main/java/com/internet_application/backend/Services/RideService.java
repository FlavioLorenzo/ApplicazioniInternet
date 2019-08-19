package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;

import java.util.List;

public interface RideService {
    List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n);
}
