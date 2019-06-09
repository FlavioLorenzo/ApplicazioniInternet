package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {
    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.line.id = ?1 " +
            "AND r.date = ?2")
    List<RideEntity> getAllRidesWithLineIdAndDate(Long lineId, Date date);
}
