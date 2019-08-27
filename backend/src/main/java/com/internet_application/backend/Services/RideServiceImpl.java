package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Repositories.RideRepository;
import com.internet_application.backend.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class RideServiceImpl implements RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired LineStopService lineStopService;
    @PersistenceContext
    private EntityManager em;

    public List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n){
        Date d = DateUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<RideEntity> rides = rideRepository.findTopNRidesWithLineIdAndDate(lineId, d, pageNumber*2);

        return rides;
    }

    public RideEntity closeStop(Long rideId, Long stopId) {
        RideEntity ride = em.find(RideEntity.class, rideId);

        /* Check if the ride already exists*/
        if (ride == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
        }

        StopEntity stop = em.find(StopEntity.class, stopId);

        /* Check if the stop already exists*/
        if (stop == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stop not found");
        }

        /* Check if the stop belongs to the ride */
        LineStopEntity lineStop =
                lineStopService.getLineStopWithLineIdAndStopIdAndDir(ride.getLine().getId(), stop.getId(), ride.getDirection());

        /* Check if it isn't too late to close the stop */
        isRidePassedOrEnded(lineStop, ride);

        ride.setLatestStop(stop);
        ride.setLatestStopTime(new Date());

        return rideRepository.save(ride);
    }

    public RideEntity openRide(Long rideId) {
        RideEntity ride = em.find(RideEntity.class, rideId);

        /* Check if the ride already exists*/
        if (ride == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
        }

        // Check if the ride is started
        if (ride.getRideBookingStatus() != RideBookingStatus.NOT_STARTED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride is already started");

        // Check if the expected day of the ride is today
        if(!DateUtils.isToday(ride.getDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ride cannot be started yet");

        ride.setRideBookingStatus(RideBookingStatus.IN_PROGRESS);
        return rideRepository.save(ride);
    }

    public RideEntity closeRide(Long rideId) {
        RideEntity ride = em.find(RideEntity.class, rideId);

        /* Check if the ride already exists*/
        if (ride == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");
        }

        isRideStarted(ride);
        isRideEnded(ride);

        if(ride.getLatestStop() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride has not reached its last destination");

        LineStopEntity lineStop =
                lineStopService.getLineStopWithLineIdAndStopIdAndDir(ride.getLine().getId(), ride.getLatestStop().getId(), ride.getDirection());

        Integer count = lineStopService.countLineStopsAfterProvidedOne(lineStop);

        if(count > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride has not reached its last destination");

        ride.setRideBookingStatus(RideBookingStatus.TERMINATED);

        return rideRepository.save(ride);
    }

    public boolean isRideEnded(RideEntity ride) {
        // Check if the ride is already terminated
        if (ride.getRideBookingStatus() == RideBookingStatus.TERMINATED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride is already terminated");

        return true;
    }

    public boolean isRideStarted(RideEntity ride) {
        // Check if the ride is started
        if (ride.getRideBookingStatus() == RideBookingStatus.NOT_STARTED)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride is not started yet");

        return true;
    }

    /**
     * Check if a RideEntity is passed through a specific stop or if the ride is terminated
     * @param stopToCheck The LineStopEntity to be checked for validity
     * @param ride The RideEntity to which the reservation is linked
     * @return True - If the ride is not terminated or if it is not passed yet from a specific stop; Otherwise, it will throw an exception.
     */
    public boolean isRidePassedOrEnded(LineStopEntity stopToCheck, RideEntity ride) {
        // Check if the ride is already terminated
        isRideEnded(ride);

        // Check if the ride has already passed through a specific stop
        if (ride.getRideBookingStatus() == RideBookingStatus.IN_PROGRESS) {
            if(ride.getLatestStop() != null) {
                Date arrivalTime = stopToCheck.getArrivalTime();
                Date latestStopArrivalTime = lineStopService
                        .getLineStopWithLineIdAndStopIdAndDir(ride.getLine().getId(), ride.getLatestStop().getId(), ride.getDirection())
                        .getArrivalTime();

                if (latestStopArrivalTime.equals(arrivalTime) || latestStopArrivalTime.after(arrivalTime))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected ride has already passed from this stop");
            }
        }

        return true;
    }

    @Override
    public List<LineStopEntity> getAvailableStops(Long rideId) {
        RideEntity ride = rideRepository.getOne(rideId);

        List<LineStopEntity> lineStops;
        if(ride.getLatestStop() != null) {
            lineStops = lineStopService.getLineStopsAvailableWithLineIdAndDir(
                            ride.getLine().getId(),
                            ride.getDirection(),
                            ride.getLatestStop().getId());
        } else {
            lineStops = lineStopService.getLineStopsWithLineIdAndDir(
                            ride.getLine().getId(),
                            ride.getDirection());
        }

        return lineStops;
    }

}
