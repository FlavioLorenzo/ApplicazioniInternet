package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Repositories.RideRepository;
import com.internet_application.backend.Serializers.RideSerializer;
import com.internet_application.backend.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class RideServiceImpl implements RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private LineStopService lineStopService;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    public List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n){
        Date d = DateUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<RideEntity> rides = rideRepository.findTopNRidesWithLineIdAndDate(lineId, d, pageNumber*2);

        return rides;
    }

    @Override
    public JsonNode getAdministeredLinesRidesFromDateToDate
            (Long userId, String fromDate, String toDate, String openOrLocked) {
        Date from = DateUtils.dateParser(fromDate);
        Date to = DateUtils.dateParser(toDate);

        // TODO: Still need to add support for administered lines
        // Based on the filter type, select only the rides that satisfy the filter
        List<RideEntity> rides;

        System.out.println(openOrLocked);

        if (openOrLocked.equals("open"))
            rides = rideRepository.getAllRidesBetweenDates(from, to, false);
        else if (openOrLocked.equals("closed"))
            rides = rideRepository.getAllRidesBetweenDates(from, to, true);
        else
            rides = rideRepository.getAllRidesBetweenDates(from, to);

        if (rides==null) return null;

        // I start to create a JSON node to hold the answer
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();

        Date curDate = from;
        while (curDate.before(to)) {
            JsonNode dayNode = mapper.createObjectNode();
            ArrayNode ridesInDayNode = mapper.createArrayNode();

            // Create the rides for the selected day
            for(int i=0; i<rides.size(); i++) {
                RideEntity ride = rides.get(i);

                if(curDate.equals(ride.getDate())) {
                    JsonNode rideNode = mapper.createObjectNode();

                    ((ObjectNode) rideNode).put("id", ride.getId().longValue());
                    ((ObjectNode) rideNode).put("date", DateUtils.dateToString(ride.getDate()));
                    ((ObjectNode) rideNode).put("direction", ride.getDirection().booleanValue());
                    ((ObjectNode) rideNode).put("lineId", ride.getLine().getId());
                    ((ObjectNode) rideNode).put("lineName", ride.getLine().getName());
                    ((ObjectNode) rideNode).put("rideBookingStatus", ride.getRideBookingStatus().getDescription());
                    ((ObjectNode) rideNode).put("locked", ride.getLocked());
                    ((ObjectNode) rideNode).put("coverage", getCoverage(ride));

                    ridesInDayNode.add(rideNode);
                } else if (curDate.before(ride.getDate())) {
                    break;
                }
            }

            // Add the rides selected to the daily rides node
            if(ridesInDayNode.size()>0) {
                ((ObjectNode) dayNode).put("date", DateUtils.dateToString(curDate));
                ((ObjectNode) dayNode).set("rides", ridesInDayNode);
                rootNode.add(dayNode);
            }

            // Move to the next date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            calendar.add(Calendar.DATE, 1);
            curDate = calendar.getTime();
        }



        return rootNode;
    }

    public String getCoverage(RideEntity ride) {
        LineStopEntity firstLse = lineStopService.getFirstLineStopWithLineIdAndDir(
                ride.getLine().getId(),
                ride.getDirection());

        if( availabilityService.hasCoverageForRideAndStop(ride, firstLse.getStop()) )
            return "complete";
        else if( availabilityService.hasCoverageForRide(ride) )
            return "partial";

        return "empty";
    }

    public JsonNode getRideAvailabilityInfo(Long userId, Long rideId) {
        RideEntity ride = rideRepository.getOne(rideId);

        if(ride == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The selected ride does not exist.");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rideNode = mapper.createObjectNode();

        // Creation of the ride object
        ObjectMapper rideMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(RideEntity.class, new RideSerializer());
        rideMapper.registerModule(module);

        JsonNode rideJson = rideMapper.valueToTree(ride);
        ((ObjectNode) rideNode).set("ride", rideJson);

        List<AvailabilityEntity> availabilities = availabilityService.getAllAvailabilitiesForRideWithId(rideId);

        ArrayNode available = mapper.createArrayNode();
        ArrayNode confirmed = mapper.createArrayNode();

        for (AvailabilityEntity availability: availabilities) {
            JsonNode userNode = mapper.createObjectNode();

            ((ObjectNode) userNode).put("availabilityId", availability.getId());
            ((ObjectNode) userNode).put("userId", availability.getUser().getId());
            ((ObjectNode) userNode).put("firstName", availability.getUser().getFirstName());
            ((ObjectNode) userNode).put("lastName", availability.getUser().getLastName());
            ((ObjectNode) userNode).put("stopId", availability.getStop().getId());
            ((ObjectNode) userNode).put("stopName", availability.getStop().getName());
            ((ObjectNode) userNode).put("shiftStatus", availability.getShiftStatus().getCode());

            if(availability.getShiftStatus().getCode() == 1) { // If the availability is new
                available.add(userNode);
            } else {
                confirmed.add(userNode);
            }
        }

        ((ObjectNode) rideNode).set("available", available);
        ((ObjectNode) rideNode).set("confirmed", confirmed);

        return rideNode;
    }

    @Override
    public RideEntity lockUnlock(Long rideId, Boolean locked) {
        RideEntity ride = em.find(RideEntity.class, rideId);

        /* Check if the ride already exists*/
        if (ride == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found");


        if( !getCoverage(ride).equals("complete"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ride can't be locked unless its coverage is " +
                    "complete");

        ride.setLocked(locked);
        return rideRepository.save(ride);
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
