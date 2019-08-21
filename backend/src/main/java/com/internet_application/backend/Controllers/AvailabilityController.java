package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.Availability;
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

    @GetMapping("/availabilities/{rideId}")
    public List<Availability> getAvailabilitiesForRideWithId(@PathVariable(value="rideId") Long rideId) {
        return availabilityService.getAllAvailabilitiesForRideWithId(rideId);
    }

    /* Get single availability */
    @GetMapping("/availability/{availabilityId}")
    public Availability getAvailabilityWithId(@PathVariable(value="availabilityId") Long availabilityId)
        throws ResponseStatusException {
        return availabilityService.getAvailabilityWithId(availabilityId);
    }

    /* Create escort availability */
    @PostMapping("/availability")
    public Availability createAvailability(@RequestBody AvailabilityPostBody availabilityPostBody)
        throws ResponseStatusException {

        if (availabilityPostBody.getRideId() == null ||
            availabilityPostBody.getUserId() == null ||
            availabilityPostBody.getStopId() == null)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Availability availability = availabilityService.buildAvailability(
                availabilityPostBody.getRideId(),
                availabilityPostBody.getUserId(),
                availabilityPostBody.getStopId()
        );
        return availabilityService.addAvailability(availability);
    }

    /* Modify escort availability */
    @PutMapping("/availability/{availabilityId}")
    public Availability putAvailability(@PathVariable(value="availabilityId") Long availabilityId,
                                        @RequestBody AvailabilityPostBody availabilityPostBody)
        throws ResponseStatusException {
        return availabilityService.modifyAvailability(
                availabilityId,
                availabilityPostBody.getRideId(),
                availabilityPostBody.getStopId()
        );
    }

    /* Delete escort availability */
    @DeleteMapping("/availability/{availabilityId}")
    public void deleteAvailabilityWithId(@PathVariable(value="availabilityId") Long availabilityId)
        throws ResponseStatusException {
        availabilityService.deleteAvailabilityWithId(availabilityId);
    }

    /* Assign availability */
    @PutMapping("/availability/{availabilityId}/confirmed")
    public void PutConfirmationStatusAvailability(@PathVariable(value="availabilityId") Long availabilityId,
                                       @RequestBody Boolean status)
            throws ResponseStatusException {
        availabilityService.setConfirmedStatusOfAvailability(availabilityId, status);
    }

    /* Availability viewed */
    @PutMapping("/availability/{availabilityId}/viewed")
    public void PutViewedStatusAvailability(@PathVariable(value="availabilityId") Long availabilityId,
                                            @RequestBody Boolean status)
            throws ResponseStatusException {
        availabilityService.setViewedStatusOfAvailability(availabilityId, status);
    }

    /* Availability locked */
    @PutMapping("/availability/{availabilityId}/locked")
    public void PutLockedAvailability(@PathVariable(value="availabilityId") Long availabilityId,
                                            @RequestBody Boolean status)
            throws ResponseStatusException {
        availabilityService.setLockedStatusOfAvailability(availabilityId, status);
    }
}
