package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;

import java.util.List;

public interface RideService {
    List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n);
    JsonNode getAdministeredLinesRidesFromDateToDate(Long userId, String fromDate, String toDate, String openOrLocked);
    RideEntity closeStop(Long rideId, Long stopId);
    RideEntity openRide(Long rideId);
    RideEntity closeRide(Long rideId);
    boolean isRideEnded(RideEntity ride);
    boolean isRideStarted(RideEntity ride);
    boolean isRidePassedOrEnded(LineStopEntity stopToCheck, RideEntity ride);
    List<LineStopEntity> getAvailableStops(Long rideId);
    JsonNode getRideAvailabilityInfo(Long userId, Long rideId);
    RideEntity lockUnlock(Long rideId, Boolean locked);
}
