package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<RideEntity, Long> {
    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.line.id = ?1 " +
            "AND r.date = ?2")
    List<RideEntity> getAllRidesWithLineIdAndDate(Long lineId, Date date);

    @Query("SELECT s FROM StopEntity s, LineStopEntity ls " +
            "WHERE s.id = ls.stop " +
            "AND ls.line.id = ?1 " +
            "AND ls.direction = ?2")
    List<StopEntity> getAllStopsOfRide(Long lineId, Boolean direction);

    @Query("Select r FROM RideEntity r " +
            "WHERE r.line.id = ?1 " +
            "AND r.date = ?2 " +
            "AND r.direction = ?3")
    List<RideEntity> getRidesWithLineIdAndDateAndDirection(Long lineId, Date d, Boolean dir);
}
