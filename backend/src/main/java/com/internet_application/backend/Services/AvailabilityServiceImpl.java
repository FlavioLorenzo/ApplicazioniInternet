package com.internet_application.backend.Services;
import com.internet_application.backend.Entities.AvailabilityEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Enums.ShiftStatus;
import com.internet_application.backend.Enums.ShiftStatusConverter;
import com.internet_application.backend.Repositories.*;
import com.internet_application.backend.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AvailabilityServiceImpl implements AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private LineStopService lineStopService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private NotificationService notificationService;

    public AvailabilityEntity buildAvailability(Long rideId, Long userId, Long stopId) {
        RideEntity ride = rideRepository.findById(rideId).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);
        StopEntity stop = stopRepository.findById(stopId).orElse(null);

        /* Check for null ride, user or stop */
        if (ride == null || user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (stop == null) {
            stop = lineStopService
                    .getFirstLineStopWithLineIdAndDir(ride.getLine().getId(), ride.getDirection())
                    .getStop();
        }

        if( lineStopService.getLineStopWithLineIdAndStopIdAndDir(
                ride.getLine().getId(), stop.getId(), ride.getDirection()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided stop is not part of the selected ride");
        }

        AvailabilityEntity availability = new AvailabilityEntity();
        availability.setRide(ride);
        availability.setUser(user);
        availability.setStop(stop);
        availability.setShiftStatus(ShiftStatus.NEW);
        return availability;
    }

    public AvailabilityEntity modifyAvailability(Long AvailabilityId, Long rideId, Long stopId)
        throws ResponseStatusException {
        AvailabilityEntity availability = getAvailabilityWithId(AvailabilityId);
        if (availability.getRide().getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        RideEntity ride = rideRepository.findById(rideId).orElse(null);
        StopEntity stop = stopRepository.findById(stopId).orElse(null);

        if (ride == null || stop == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if( lineStopService.getLineStopWithLineIdAndStopIdAndDir(
                ride.getLine().getId(), stop.getId(), ride.getDirection()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The provided stop is not part of the selected ride");
        }

        availability.setRide(ride);
        availability.setStop(stop);
        availability.setShiftStatus(ShiftStatus.NEW);
        availabilityRepository.save(availability);
        return availability;
    }

    public AvailabilityEntity addAvailability(AvailabilityEntity availability) {
        if (availabilityRepository.findAvailabilityByRideAndUser(
                availability.getRide().getId(),
                availability.getUser().getId()) == null)
        {
            availabilityRepository.save(availability);
        }
        else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return availability;
    }

    public AvailabilityEntity setStatusOfAvailability(Long availabilityId, Integer status) {
        AvailabilityEntity availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        if (availability.getRide().getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This ride is closed. " +
                    "Availabilities for this ride can no longer be modified.");

        ShiftStatus currentStatus = availability.getShiftStatus();

        if(status == ShiftStatus.CONFIRMED.getCode()) {
            if(currentStatus != ShiftStatus.NEW)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot set status to Confirmed since the current status is " + currentStatus.getDescription());

            if(availabilityRepository.getConfirmedAvailabilitiesForUserAndDateAndDirection(
                    availability.getUser().getId(),
                    availability.getRide().getDate(),
                    availability.getRide().getDirection()).size() > 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot set status to Confirmed since the selected user has already been confirmed for another " +
                                "ride at the same time.");
        }

        if(status == ShiftStatus.VIEWED.getCode() && currentStatus != ShiftStatus.CONFIRMED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot set status to Viewed since the current status is " + currentStatus.getDescription());
        }

        ShiftStatusConverter ssc = new ShiftStatusConverter();

        availability.setShiftStatus(ssc.convertToEntityAttribute(status));
        availabilityRepository.save(availability);

        // If the shift has been confirmed, send a notification to the companion
        if(status == ShiftStatus.CONFIRMED.getCode()) {
            notificationService.createNotification(
                    availability.getUser().getId(),
                    "Your availability for the ride of line " + availability.getRide().getLine().getId() +
                            " scheduled for " + DateUtils.dateToString(availability.getRide().getDate()) +
                            ", has been taken into account. " +
                            "Please go to your Shift Definition page to acknowledge the reception of the message.",
                    "/shift-definition/line/" + availability.getRide().getLine().getId() +
                            "/" + DateUtils.dateToSendString(availability.getRide().getDate())
            );
        }

        // If the shift has been viewed, send a notification to the admin of the line
        if(status == ShiftStatus.VIEWED.getCode()) {
            notificationService.createNotification(
                    availability.getRide().getLine().getAdmin().getId(),
                    "User " + availability.getUser().getFirstName() +
                            " " + availability.getUser().getLastName() +
                            " has received the confirmation for its availability for the ride of line " + availability.getRide().getLine().getId() +
                            " scheduled for " + DateUtils.dateToString(availability.getRide().getDate()) + ". " +
                            "Please go to the Shift Consolidation page to lock the ride.",
                    "/shift-consolidation/" + availability.getRide().getId()
            );
        }

        return availability;
    }

    public List<AvailabilityEntity> getAllAvailabilitiesForRideWithId(Long rideId) {
        return availabilityRepository.getAllAvailabilitiesForRideWithId(rideId);
    }

    public AvailabilityEntity getAvailabilityWithId(Long availabilityId) {
        AvailabilityEntity availability = availabilityRepository.findById(availabilityId).orElse(null);
        if (availability == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return availability;
    }

    @Override
    public List<AvailabilityEntity> getNAvailabilitiesByUserFromDate(Long lineId, Long userId, String date, Integer n) {
        Date d = DateUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<AvailabilityEntity> availabilities =
                availabilityRepository.getFirstNReservationsWithLineIdAndUserIdAndDate(lineId, userId, d, pageNumber * 2);

        return availabilities;
    }

    public void deleteAvailabilityWithId(Long availabilityId) {
        AvailabilityEntity availability = getAvailabilityWithId(availabilityId);
        if (availability.getRide().getLocked())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        availabilityRepository.delete(availability);
    }

    public Boolean hasCoverageForRideAndStop(RideEntity ride, StopEntity stop) {
        if( availabilityRepository.countViewedAvailabilitiesForRideAndStop(ride.getId(), stop.getId()) == 0 ) {
            return false;
        }
        return true;
    }

    public Boolean hasCoverageForRide(RideEntity ride) {
        if( availabilityRepository.countViewedAvailabilitiesForRide(ride.getId()) == 0 ) {
            return false;
        }
        return true;
    }
}
