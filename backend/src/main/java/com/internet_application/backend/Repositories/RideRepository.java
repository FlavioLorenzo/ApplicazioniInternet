package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.line.id = ?1 " +
            "AND r.date >= ?2 " +
            "ORDER BY r.date ASC")
    List<RideEntity> getTopRidesWithLineIdAndDate(Long lineId, Date date, Pageable pageable);

    default List<RideEntity> findTopNRidesWithLineIdAndDate(Long lineId, Date date, int n) {
        return getTopRidesWithLineIdAndDate(lineId, date, PageRequest.of(0, n));
    }

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

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?1 AND ?2 " +
            "AND r.locked = ?3 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllRidesBetweenDates(Date fromDate, Date toDate, Boolean locked);

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?1 AND ?2 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllRidesBetweenDates(Date fromDate, Date toDate);
}
