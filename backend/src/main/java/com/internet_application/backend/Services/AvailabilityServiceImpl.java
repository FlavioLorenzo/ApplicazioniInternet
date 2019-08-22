package com.internet_application.backend.Services;
import com.internet_application.backend.Entities.Availability;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StopRepository stopRepository;

    public Availability buildAvailability(Long rideId, Long userId, Long stopId) {
        RideEntity ride = rideRepository.findById(rideId).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);
        StopEntity stop = stopRepository.findById(stopId).orElse(null);

        /* Check for null ride, user or stop */
        if (ride == null || user == null || stop == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        //TODO Check why the hack this control does not work
        //isStopPresentInRide(ride, stop);

        Availability availability = new Availability();
        availability.setRide(ride);
        availability.setUser(user);
        availability.setStop(stop);
        availability.setConfirmed(false);
        availability.setViewed(false);
        availability.setLocked(false);
        return availability;
    }

    public Availability modifyAvailability(Long AvailabilityId, Long rideId, Long stopId)
        throws ResponseStatusException {
        Availability availability = getAvailabilityWithId(AvailabilityId);
        if (availability.getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        RideEntity ride = rideRepository.findById(rideId).orElse(null);
        StopEntity stop = stopRepository.findById(stopId).orElse(null);

        if (ride == null || stop == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        //TODO Check why the hack this control does not work
        //isStopPresentInRide(ride, stop);

        availability.setRide(ride);
        availability.setStop(stop);
        availabilityRepository.save(availability);
        return availability;
    }

    public Availability addAvailability(Availability availability) {
        if (availabilityRepository.findAvailabilityByRideAndUser(
                availability.getRide().getId(),
                availability.getUser().getId()) == null)
        {
            availabilityRepository.save(availability);
        }
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return availability;
    }

    public Availability setConfirmedStatusOfAvailability(Long availabilityId, Boolean confirmedStatus) {
        Availability availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (availability.getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        availability.setConfirmed(confirmedStatus);
        availabilityRepository.save(availability);
        return availability;
    }

    public Availability setViewedStatusOfAvailability(Long availabilityId, Boolean viewedStatus) {
        Availability availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /* Check if the availability is confirmed. If it is not reject the request */
        if (!availability.getConfirmed() || availability.getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        availability.setViewed(viewedStatus);
        availabilityRepository.save(availability);
        return availability;
    }

    public Availability setLockedStatusOfAvailability(Long availabilityId, Boolean lockedStatus) {
        Availability availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        availability.setLocked(lockedStatus);
        availabilityRepository.save(availability);
        return availability;
    }

    public List<Availability> getAllAvailabilitiesForRideWithId(Long rideId) {
        return availabilityRepository.getAllAvailabilitiesForRideWithId(rideId);
    }

    public Availability getAvailabilityWithId(Long availabilityId) {
        Availability availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return availability;
    }

    public void deleteAvailabilityWithId(Long availabilityId) {
        Availability availability = getAvailabilityWithId(availabilityId);
        if (availability.getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        availabilityRepository.delete(availability);
    }

    // TODO NOT WORKING
    /* Utility to check if the stop is present inside the ride */
    private void isStopPresentInRide(RideEntity ride, StopEntity stop)
            throws ResponseStatusException {
        if (!ride.getDirection()) {
            ride.getLine().getOutwordStops().forEach(lineStopEntity -> {
                if (lineStopEntity.getStop().equals(stop)) return; });
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (ride.getDirection()) {
            ride.getLine().getOutwordStops().forEach(lineStopEntity -> {
                if (lineStopEntity.getStop().equals(stop)) return; });
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}