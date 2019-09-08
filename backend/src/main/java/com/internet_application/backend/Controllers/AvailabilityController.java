package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.AvailabilityEntity;
import com.internet_application.backend.PostBodies.AvailabilityPostBody;
import com.internet_application.backend.Services.AvailabilityService;
import com.internet_application.backend.Services.BusLineService;
import com.internet_application.backend.Services.PrincipalService;
import com.internet_application.backend.Services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/*
 * SECURITY POLICY
 * System admin -> Permission for everything
 * Admin -> GET on everything. Checks on POST, PUT and DELETE
 * Escort -> GET, POST, PUT, DELETE on owned availabilities. No permission on others
 * User -> No permission
 * */

@CrossOrigin
@RestController
public class AvailabilityController {
    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    RideService rideService;
    @Autowired
    BusLineService busLineService;
    @Autowired
    PrincipalService principalService;


    @GetMapping("/availabilities/{rideId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN','ADMIN')")
    public List<AvailabilityEntity> getAvailabilitiesForRideWithId(@PathVariable(value="rideId") Long rideId) {
        return availabilityService.getAllAvailabilitiesForRideWithId(rideId);
    }

    /* Get single availability */
    @GetMapping("/availability/{availabilityId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public AvailabilityEntity getAvailabilityWithId(
            Principal principal,
            @PathVariable(value="availabilityId") Long availabilityId
    )
        throws ResponseStatusException {
        AvailabilityEntity availability = availabilityService.getAvailabilityWithId(availabilityId);

        /* Security check */
        if (principalService.IsUserEscort(principal) &&
            !principalService.doesUserMatchPrincipal(principal, availability.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return availability;
    }

    @GetMapping("/availabilities/{line_id}/{user_id}/{date}/{n}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public List<AvailabilityEntity> getNReservationsByUserFromDate(Principal principal,
                                                                   @PathVariable(value="line_id") Long lineId,
                                                                   @PathVariable(value="user_id") Long userId,
                                                                   @PathVariable(value="date") String date,
                                                                   @PathVariable(value="n") Integer n)
            throws ResponseStatusException {
        /* Security check */
        if (principalService.IsUserEscort(principal) &&
                !principalService.doesUserMatchPrincipal(principal, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        List<AvailabilityEntity> availabilities =
                availabilityService.getNAvailabilitiesByUserFromDate(lineId, userId, date, n);
        return availabilities;
    }
    
    /* Create escort availability */
    @PostMapping("/availability")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ESCORT')")
    public AvailabilityEntity createAvailability(
            Principal principal,
            @RequestBody AvailabilityPostBody availabilityPostBody
    )
        throws ResponseStatusException {

        if (availabilityPostBody.getRideId() == null ||
            availabilityPostBody.getUserId() == null ||
            availabilityPostBody.getStopId() == null)
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        /* Security check */
        if (principalService.IsUserEscort(principal) &&
                !principalService.doesUserMatchPrincipal(principal, availabilityPostBody.getUserId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        AvailabilityEntity availability = availabilityService.buildAvailability(
                availabilityPostBody.getRideId(),
                availabilityPostBody.getUserId(),
                availabilityPostBody.getStopId()
        );
        return availabilityService.addAvailability(availability);
    }

    /* Modify escort availability */
    @PutMapping("/availability/{availabilityId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public AvailabilityEntity putAvailability(Principal principal,
                                              @PathVariable(value="availabilityId") Long availabilityId,
                                              @RequestBody AvailabilityPostBody availabilityPostBody)
        throws ResponseStatusException {

        /* Security check */
        AvailabilityEntity availability = availabilityService.getAvailabilityWithId(availabilityId);
        if (principalService.IsUserEscort(principal) &&
                !principalService.doesUserMatchPrincipal(principal, availability.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return availabilityService.modifyAvailability(
                availabilityId,
                availabilityPostBody.getRideId(),
                availabilityPostBody.getStopId()
        );
    }

    /* Delete escort availability */
    @DeleteMapping("/availability/{availabilityId}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ESCORT')")
    public void deleteAvailabilityWithId(
            Principal principal,
            @PathVariable(value="availabilityId") Long availabilityId
    )
        throws ResponseStatusException {

        /* Security check */
        AvailabilityEntity availability = availabilityService.getAvailabilityWithId(availabilityId);
        if (principalService.IsUserEscort(principal) &&
                !principalService.doesUserMatchPrincipal(principal, availability.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        availabilityService.deleteAvailabilityWithId(availabilityId);
    }

    /* Assign availability */
    @PutMapping("/availability/{availabilityId}/{statusCode}")
    @PreAuthorize("hasAnyRole('SYS_ADMIN', 'ADMIN', 'ESCORT')")
    public void PutAvailabilityStatus(
            Principal principal,
            @PathVariable(value="availabilityId") Long availabilityId,
            @PathVariable(value="statusCode") Integer status)
            throws ResponseStatusException {
        /* Security check */
        AvailabilityEntity availability = availabilityService.getAvailabilityWithId(availabilityId);
        if (principalService.IsUserEscort(principal) &&
                !principalService.doesUserMatchPrincipal(principal, availability.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (principalService.IsUserAdmin(principal) &&
                !principalService.IsUserAdminOfLine(principal, availability.getRide().getLine().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        availabilityService.setStatusOfAvailability(availabilityId, status);
    }
}
