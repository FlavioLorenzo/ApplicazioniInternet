package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Enums.ShiftStatus;
import com.internet_application.backend.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
public class PrincipalServiceImpl implements PrincipalService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BusLineRepository busLineRepository;
    @Autowired
    RideRepository rideRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;
    @Autowired
    ChildRepository childRepository;
    @Autowired
    RideService rideService;
    @Autowired
    ReservationService reservationService;

    /*
    * Get user from principal
    * If the principal is not provided or the user does not exist throw 401
    * userRepository automatically throws it.
    * */
    @Override
    public UserEntity getUserFromPrincipal(Principal principal)
        throws ResponseStatusException {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        UserEntity user = userRepository.findByEmail(principal.getName());
        return user;
    }

    @Override
    public boolean doesUserMatchPrincipal(Principal principal, Long userId) {
        UserEntity user = getUserFromPrincipal(principal);
        if (user.getId().equals(userId))
            return true;
        return false;
    }

    @Override
    public boolean IsUserSystemAdmin(Principal principal)
        throws ResponseStatusException {
        UserEntity user = getUserFromPrincipal(principal);
        if (user.getRole().getId().equals(1L))
            return true;
        return false;
    }

    @Override
    public boolean IsUserAdmin(Principal principal)
        throws ResponseStatusException {
        UserEntity user = getUserFromPrincipal(principal);
        if (user.getRole().getId().equals(2L))
            return true;
        return false;
    }

    @Override
    public boolean IsUserAdminOfLine(Principal principal, Long lineId)
        throws ResponseStatusException {
        /* Get the user and the line */
        UserEntity user = getUserFromPrincipal(principal);
        BusLineEntity busLineEntity = busLineRepository.findById(lineId).orElse(null);
        if (busLineEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /* Check if user is admin and if he manages the line */
        if (user.getRole().getId().equals(2L) &&
            user.getAdministeredBuslines().contains(busLineEntity))
            return true;
        return false;
    }

    @Override
    public boolean IsUserEscort(Principal principal)
        throws ResponseStatusException {
        UserEntity user = getUserFromPrincipal(principal);
        if (user.getRole().getId().equals(3L))
            return true;
        return false;
    }

    @Override
    public boolean IsUserEscortInRide(Principal principal, Long rideId)
        throws ResponseStatusException {
        /* Get the user, the ride and the availability */
        UserEntity user = getUserFromPrincipal(principal);
        RideEntity rideEntity = rideRepository.findById(rideId).orElse(null);
        AvailabilityEntity availabilityEntity = availabilityRepository
                .findAvailabilityByRideAndUser(rideId, user.getId());
        /* If ride is null throw 404 */
        if (rideEntity == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /*
        * If availability is null it's not an escort
        * If the availability is not in state viewed it's not an escort
         * */
        return (availabilityEntity != null && availabilityEntity.getShiftStatus().equals(ShiftStatus.VIEWED));
    }

    @Override
    public boolean IsUserParent(Principal principal)
        throws ResponseStatusException {
        UserEntity user = getUserFromPrincipal(principal);
        if (user.getRole().getId().equals(4L))
            return true;
        return false;
    }

    @Override
    public boolean IsUserParentOfChild(Principal principal, Long childId)
        throws ResponseStatusException {
        /* Get user and parent */
        UserEntity user = getUserFromPrincipal(principal);
        ChildEntity child = childRepository.findById(childId).orElse(null);
        /* If child does not exist throw 404 */
        if (child == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /*if (user.getRole().getId().equals(4L) &&
            child.getParent().equals(user))
            return true;*/
        return child.getParent().equals(user);
    }

    @Override
    public boolean canUserEditRide(Principal principal, Long rideId) {
        if(!IsUserEscortInRide(principal, rideId)) {
            if (IsUserEscort(principal))
                return false;

            Long lineId = rideService.getRide(rideId).getLine().getId();
            if(IsUserAdmin(principal) &&
                    !IsUserAdminOfLine(principal,lineId))
                return false;

        }

        return true;
    }

    @Override
    public boolean canUserEditReservation(Principal principal, Long resId) {
        ReservationEntity reservation = reservationService.getReservationById(resId);

        if(reservation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        /* If the user is the parent of the child, there is no problem */
        if(!IsUserParentOfChild(principal, reservation.getChild().getId())) {
            /* Parent of child */
            if (IsUserParent(principal))
                return false;

            /* Escort in ride */
            if (!IsUserEscortInRide(principal, reservation.getRide().getId())) {
                if(IsUserEscort(principal))
                    return false;

                /* Admin of line */
                if (IsUserAdmin(principal) &&
                        !IsUserAdminOfLine(principal, reservation.getRide().getLine().getId()))
                    return false;
            }
        }

        return true;
    }
}
