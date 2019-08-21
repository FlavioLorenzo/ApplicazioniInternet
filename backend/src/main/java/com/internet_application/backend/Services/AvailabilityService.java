package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.Availability;

import java.util.List;

public interface AvailabilityService {
    Availability buildAvailability(Long rideId, Long userId, Long stopId);

    public Availability modifyAvailability(Long AvailabilityId, Long rideId, Long stopId);

    Availability addAvailability(Availability availability);

    Availability setConfirmedStatusOfAvailability(Long availabilityId, Boolean confirmedStatus);

    Availability setViewedStatusOfAvailability(Long availabilityId, Boolean viewedStatus);

    Availability setLockedStatusOfAvailability(Long availabilityId, Boolean lockedStatus);

    List<Availability> getAllAvailabilitiesForRideWithId(Long rideId);

    Availability getAvailabilityWithId(Long availabilityId);

    void deleteAvailabilityWithId(Long availabilityId);
}
