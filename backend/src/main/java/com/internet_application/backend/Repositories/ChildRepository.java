package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
    @Query("Select c FROM ChildEntity c " +
            " WHERE c.parent.id = ?1")
    List<ChildEntity> getChildrenWithParentId(Long userId);
}
