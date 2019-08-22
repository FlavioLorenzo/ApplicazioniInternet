package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.UserEntity;

import java.util.List;

public interface ChildService {
    void save(ChildEntity child);
    void saveAll(List<ChildEntity> children);

    List<ChildEntity> getAllChildren();
}
