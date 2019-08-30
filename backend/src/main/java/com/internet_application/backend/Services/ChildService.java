package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.ChildEntity;

import java.util.List;

public interface ChildService {
    ChildEntity save(ChildEntity child);
    void saveAll(List<ChildEntity> children);

    ChildEntity getChildWithId(Long childId);
    void deleteChildWithId(Long childId);
    ChildEntity buildChild(
            Long parentId,
            String firstName,
            String lastName,
            String phone
    );

    List<ChildEntity> getAllChildren();
    List<ChildEntity> getAllChildrenWithParentId(Long userId);
}
