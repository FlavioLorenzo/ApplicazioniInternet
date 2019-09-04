package com.internet_application.backend.Services;

import java.security.Principal;

public interface PrincipalService {
    boolean IsUserSystemAdmin(Principal principal);
    boolean IsUserAdmin(Principal principal);
    boolean IsUserAdminOfLine(Principal principal, Long lineId);
    boolean IsUserEscort(Principal principal);
    boolean IsUserEscortInRide(Principal principal, Long rideId);
    boolean IsUserParent(Principal principal);
    boolean IsUserParentOfChild(Principal principal, Long childId);
}
