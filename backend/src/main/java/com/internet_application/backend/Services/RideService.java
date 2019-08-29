package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;

import java.util.List;

public interface RideService {
    List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n);
    RideEntity closeStop(Long rideId, Long stopId);
    RideEntity openRide(Long rideId);
    RideEntity closeRide(Long rideId);
    boolean isRideEnded(RideEntity ride);
    boolean isRideStarted(RideEntity ride);
    boolean isRidePassedOrEnded(LineStopEntity stopToCheck, RideEntity ride);
    List<LineStopEntity> getAvailableStops(Long rideId);
}
