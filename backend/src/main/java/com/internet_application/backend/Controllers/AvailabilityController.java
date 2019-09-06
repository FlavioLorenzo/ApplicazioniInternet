package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.AvailabilityEntity;
import com.internet_application.backend.PostBodies.AvailabilityPostBody;
import com.internet_application.backend.Services.AvailabilityService;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
public class AvailabilityController {
    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    RideService rideService;
    @Autowired
    BusLineService busLineService;

    // No parent
    // TODO forse scorta
    @GetMapping("/availabilities/{rideId}")
    public List<AvailabilityEntity> getAvailabilitiesForRideWithId(@PathVariable(value="rideId") Long rideId) {
        return availabilityService.getAllAvailabilitiesForRideWithId(rideId);
    }

    /* Get single availability */
    // No parent
    // Escort can only get their own
    @GetMapping("/availability/{availabilityId}")
    public AvailabilityEntity getAvailabilityWithId(@PathVariable(value="availabilityId") Long availabilityId)
        throws ResponseStatusException {
        return availabilityService.getAvailabilityWithId(availabilityId);
    }

    // No parent
    // Escort can only get their own
    @GetMapping("/availabilities/{line_id}/{user_id}/{date}/{n}")
    public List<AvailabilityEntity> getNReservationsByUserFromDate(@PathVariable(value="line_id") Long lineId,
                                                                   @PathVariable(value="user_id") Long userId,
                                                                   @PathVariable(value="date") String date,
                                                                   @PathVariable(value="n") Integer n)
            throws ResponseStatusException {
        List<AvailabilityEntity> availabilities =
                availabilityService.getNAvailabilitiesByUserFromDate(lineId, userId, date, n);
        return availabilities;
    }
    
    /* Create escort availability */
    // Only escort
    @PostMapping("/availability")
    public AvailabilityEntity createAvailability(@RequestBody AvailabilityPostBody availabilityPostBody)
        throws ResponseStatusException {

        if (availabilityPostBody.getRideId() == null ||
            availabilityPostBody.getUserId() == null ||
            availabilityPostBody.getStopId() == null)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        AvailabilityEntity availability = availabilityService.buildAvailability(
                availabilityPostBody.getRideId(),
                availabilityPostBody.getUserId(),
                availabilityPostBody.getStopId()
        );
        return availabilityService.addAvailability(availability);
    }

    /* Modify escort availability */
    // Only escort who owns the availability
    @PutMapping("/availability/{availabilityId}")
    public AvailabilityEntity putAvailability(@PathVariable(value="availabilityId") Long availabilityId,
                                              @RequestBody AvailabilityPostBody availabilityPostBody)
        throws ResponseStatusException {
        return availabilityService.modifyAvailability(
                availabilityId,
                availabilityPostBody.getRideId(),
                availabilityPostBody.getStopId()
        );
    }

    /* Delete escort availability */
    // Only escort who owns the availability
    @DeleteMapping("/availability/{availabilityId}")
    public void deleteAvailabilityWithId(@PathVariable(value="availabilityId") Long availabilityId)
        throws ResponseStatusException {
        availabilityService.deleteAvailabilityWithId(availabilityId);
    }

    /* Assign availability */
    // Escort who owns it
    // Admin of line
    @PutMapping("/availability/{availabilityId}/{statusCode}")
    public void PutAvailabilityStatus(@PathVariable(value="availabilityId") Long availabilityId,
                @PathVariable(value="statusCode") Integer status)
            throws ResponseStatusException {
        availabilityService.setStatusOfAvailability(availabilityId, status);
    }
}
