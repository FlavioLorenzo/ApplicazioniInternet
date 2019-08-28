package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.AvailabilityEntity;

import java.util.List;

public interface AvailabilityService {
    AvailabilityEntity buildAvailability(Long rideId, Long userId, Long stopId);

    AvailabilityEntity modifyAvailability(Long AvailabilityId, Long rideId, Long stopId);

    AvailabilityEntity addAvailability(AvailabilityEntity availability);

    AvailabilityEntity setStatusOfAvailability(Long availabilityId, Integer status);

    List<AvailabilityEntity> getAllAvailabilitiesForRideWithId(Long rideId);

    AvailabilityEntity getAvailabilityWithId(Long availabilityId);

    List<AvailabilityEntity> getNAvailabilitiesByUserFromDate(Long lineId, Long userId, String date, Integer n);

    void deleteAvailabilityWithId(Long availabilityId);

}
