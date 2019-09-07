package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.PostBodies.ChildPostBody;
import com.internet_application.backend.Services.ChildService;
import com.internet_application.backend.Services.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/*
* SECURITY POLICY
* Everyone allowed.
* Users can use methods only on owned children
* */

@CrossOrigin()
@RestController
public class ChildController {
    @Autowired
    ChildService childService;
    @Autowired
    PrincipalService principalService;

    @GetMapping("/children")
    public List<ChildEntity> getAllChildren(Principal principal)
        throws ResponseStatusException {
        /* Disallowed to normal user */
        if (principalService.IsUserParent(principal))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return childService.getAllChildren();
    }

    @GetMapping("/children/{userId}")
    public List<ChildEntity> getAllChildrenWithParentId(
            Principal principal,
            @PathVariable(value="userId") Long userId
    )
        throws  ResponseStatusException {
        /* Restricted to normal user */
        if (principalService.IsUserParent(principal) &&
            !principalService.doesUserMatchPrincipal(principal, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return childService.getAllChildrenWithParentId(userId);
    }

    @GetMapping("/children/free/{date}/{direction}")
    public List<ChildEntity> getFreeChildren(
            Principal principal,
            @PathVariable(value="date") String date,
            @PathVariable(value="direction") Boolean direction)
            throws ResponseStatusException {
        /* Disallowed to normal user */
        if (principalService.IsUserParent(principal))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return childService.getFreeChildren(date, direction);
    }

    @GetMapping("/child/{childId}")
    public ChildEntity getChildWithId(
            Principal principal,
            @PathVariable(value="childId") Long childId)
            throws  ResponseStatusException {
        /* Restricted to normal user */
        if (principalService.IsUserParent(principal) &&
            !principalService.IsUserParentOfChild(principal, childId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return childService.getChildWithId(childId);
    }

    @DeleteMapping("/child/{childId}")
    public void deleteChildWithId(
            Principal principal,
            @PathVariable(value="childId") Long childId
    )
            throws  ResponseStatusException {
        /* Sysadmin allowed only */
        if (!principalService.IsUserSystemAdmin(principal))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        childService.deleteChildWithId(childId);
    }


    @PostMapping("/child")
    public ChildEntity createChild(
            Principal principal,
            @RequestBody ChildPostBody childPostBody
    )
            throws ResponseStatusException {
        /* For normal users principal must match post data */
        if (principalService.IsUserParent(principal) &&
            !principalService.doesUserMatchPrincipal(
                    principal, childPostBody.getUserId()
            )
        )
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        ChildEntity child = childService.buildChild(
                childPostBody.getUserId(),
                childPostBody.getFirstName(),
                childPostBody.getLastName(),
                childPostBody.getPhone()
        );
        return childService.save(child);
    }
}

