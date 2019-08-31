package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Repositories.*;
import com.internet_application.backend.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class ChildServiceImpl implements ChildService {
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public ChildEntity getChildWithId(Long childId) {
        ChildEntity child = childRepository.findById(childId).orElse(null);
        if (child == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return child;
    }

    public void deleteChildWithId(Long childId) {
        ChildEntity child = childRepository.findById(childId).orElse(null);
        if (child == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        childRepository.delete(child);
    }

    public ChildEntity buildChild(
            Long parentId,
            String firstName,
            String lastName,
            String phone
            ) {
        /* Check empty fields*/
        if (parentId == null ||
            firstName == null ||
            lastName == null ||
            phone == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        /* Check parent user exists */
        UserEntity user = userRepository.findById(parentId).orElse(null);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        /* Create child */
        ChildEntity child = new ChildEntity();
        child.setParent(user);
        child.setFirstName(firstName);
        child.setLastName(lastName);
        child.setPhone(phone);
        return child;
    }

    public List<ChildEntity> getAllChildrenWithParentId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return childRepository.getChildrenWithParentId(userId);
    }

    public List<ChildEntity> getAllChildren() {
        return childRepository.findAll();
    }

    public List<ChildEntity> getFreeChildren(String date, Boolean direction) {
        Date d = DateUtils.dateParser(date);
        return childRepository.getFreeChildrenByDateAndDirection(d, direction);
    }

    @Override
    public ChildEntity save(ChildEntity child) {
        childRepository.save(child);
        return child;
    }

    @Override
    public void saveAll(List<ChildEntity> children) {
        for(ChildEntity child: children) {
            save(child);
        }
    }
}
