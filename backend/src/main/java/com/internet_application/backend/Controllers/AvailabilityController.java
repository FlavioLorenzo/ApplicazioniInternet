package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.Availability;
import com.internet_application.backend.PostBodies.AvailabilityPostBody;
import com.internet_application.backend.Services.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
public class AvailabilityController {
    @Autowired
    AvailabilityService availabilityService;

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
        availabilityService.deleteAvailabilityWithId(availabilityId);
        return availabilityService.addAvailability(availabilityService.buildAvailability(
                availabilityPostBody.getRideId(),
                availabilityPostBody.getUserId(),
                availabilityPostBody.getStopId()
        ));
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
