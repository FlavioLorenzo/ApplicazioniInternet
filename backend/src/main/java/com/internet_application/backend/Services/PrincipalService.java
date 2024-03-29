package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.UserEntity;

import java.security.Principal;

public interface PrincipalService {
    UserEntity getUserFromPrincipal(Principal principal);
    boolean doesUserMatchPrincipal(Principal principal, Long userId);
    boolean IsUserSystemAdmin(Principal principal);
    boolean IsUserAdmin(Principal principal);
    boolean IsUserAdminOfLine(Principal principal, Long lineId);
    boolean IsUserEscort(Principal principal);
    boolean IsUserEscortInRide(Principal principal, Long rideId);
    boolean IsUserParent(Principal principal);
    boolean IsUserParentOfChild(Principal principal, Long childId);
    boolean canUserEditRide(Principal principal, Long rideId);
    boolean canUserEditReservation(Principal principal, Long resId);
}
