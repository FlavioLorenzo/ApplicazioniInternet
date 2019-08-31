package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
    @Query("Select c FROM ChildEntity c " +
            " WHERE c.parent.id = ?1")
    List<ChildEntity> getChildrenWithParentId(Long userId);

    @Query( "SELECT c FROM ChildEntity  c " +
            "WHERE c.id NOT IN (" +
            "   SELECT r.child.id FROM ReservationEntity r " +
            "   WHERE r.ride.date = ?1 " +
            "   AND r.ride.direction = ?2)"
    )
    List<ChildEntity> getFreeChildrenByDateAndDirection(Date d, Boolean direction);
}
