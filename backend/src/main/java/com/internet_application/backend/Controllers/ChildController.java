package com.internet_application.backend.Controllers;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.PostBodies.ChildPostBody;
import com.internet_application.backend.Services.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin()
@RestController
public class ChildController {
    @Autowired
    ChildService childService;

    @GetMapping("/children")
    public List<ChildEntity> getAllChildren()
        throws ResponseStatusException {
        return childService.getAllChildren();
    }

    @GetMapping("/children/{userId}")
    public List<ChildEntity> getAllChildrenWithParentId(@PathVariable(value="userId") Long userId)
        throws  ResponseStatusException {
        return childService.getAllChildrenWithParentId(userId);
    }

    @GetMapping("/children/free/{date}/{direction}")
    public List<ChildEntity> getFreeChildren(
            @PathVariable(value="date") String date,
            @PathVariable(value="direction") Boolean direction)
            throws ResponseStatusException {
        return childService.getFreeChildren(date, direction);
    }

    @GetMapping("/child/{childId}")
    public ChildEntity getChildWithId(@PathVariable(value="childId") Long childId)
            throws  ResponseStatusException {
        return childService.getChildWithId(childId);
    }

    @DeleteMapping("/child/{childId}")
    public void deleteChildWithId(@PathVariable(value="childId") Long childId)
            throws  ResponseStatusException {
        childService.deleteChildWithId(childId);
    }

    @PostMapping("/child")
    public ChildEntity createChild(@RequestBody ChildPostBody childPostBody)
            throws ResponseStatusException {
        ChildEntity child = childService.buildChild(
                childPostBody.getUserId(),
                childPostBody.getFirstName(),
                childPostBody.getLastName(),
                childPostBody.getPhone()
        );
        return childService.save(child);
    }
}

