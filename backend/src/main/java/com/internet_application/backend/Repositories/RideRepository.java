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
            "AND ls.direction = ?2 " +
            "ORDER BY ls.arrivalTime ASC")
    List<StopEntity> getAllStopsOfRide(Long lineId, Boolean direction);

    @Query("Select r FROM RideEntity r " +
            "WHERE r.line.id = ?1 " +
            "AND r.date = ?2 " +
            "AND r.direction = ?3")
    List<RideEntity> getRidesWithLineIdAndDateAndDirection(Long lineId, Date d, Boolean dir);

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.id IN (" +
                "SELECT a.ride.id FROM AvailabilityEntity a " +
                "WHERE a.user.id = ?1 " +
                "AND a.shiftStatus = com.internet_application.backend.Enums.ShiftStatus.VIEWED) " +
            "AND r.date >= ?2 " +
            "AND r.locked = true " +
            "AND r.rideBookingStatus <> com.internet_application.backend.Enums.RideBookingStatus.TERMINATED " +
            "ORDER BY r.date, r.direction ASC")
    List<RideEntity> getTopLockedRidesFromUserAndDate(Long lineId, Date date, Pageable pageable);

    default List<RideEntity> getTopNLockedRidesFromUserAndDate(Long userId, Date date, int n) {
        return getTopLockedRidesFromUserAndDate(userId, date, PageRequest.of(0, n));
    }

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?2 AND ?3 " +
            "AND r.line.admin.id = ?1 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllAdministeredRidesBetweenDates(Long userId, Date from, Date to);

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?2 AND ?3 " +
            "AND r.line.admin.id = ?1 " +
            "AND r.locked = ?4 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllAdministeredRidesBetweenDates(Long userId, Date from, Date to, boolean locked);

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?1 AND ?2 " +
            "AND r.locked = ?3 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllRidesBetweenDates(Date fromDate, Date toDate, boolean locked);

    @Query("SELECT r FROM RideEntity r " +
            "WHERE r.date BETWEEN ?1 AND ?2 " +
            "ORDER BY r.date, r.line.id, r.direction ASC ")
    List<RideEntity> getAllRidesBetweenDates(Date fromDate, Date toDate);

}

/*
*
* (" +
                "r.rideBookingStatus = com.internet_application.backend.Enums.RideBookingStatus.NOT_STARTED " +
                "OR r.rideBookingStatus = com.internet_application.backend.Enums.RideBookingStatus.IN_PROGRESS) " +*/