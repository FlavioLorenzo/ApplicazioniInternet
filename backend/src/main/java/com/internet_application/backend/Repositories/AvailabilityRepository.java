package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.AvailabilityEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {
    @Query("SELECT av FROM AvailabilityEntity av " +
            "WHERE av.ride.id = ?1 ")
    List<AvailabilityEntity> getAllAvailabilitiesForRideWithId(Long rideId);

    @Query("SELECT av FROM AvailabilityEntity av " +
            "WHERE av.ride.id = ?1 " +
            "AND av.user.id = ?2")
    AvailabilityEntity findAvailabilityByRideAndUser(Long rideId, Long userId);

    @Query("SELECT a FROM AvailabilityEntity a " +
            "WHERE a.ride.line.id = ?1 " +
            "AND a.user.id = ?2 " +
            "AND a.ride.date >= ?3 " +
            "ORDER BY a.ride.date ASC")
    List<AvailabilityEntity> getFirstReservationsWithLineIdAndChildIdAndDate(Long lineId, Long userId, Date date, Pageable pageable);

    default List<AvailabilityEntity> getFirstNReservationsWithLineIdAndUserIdAndDate(Long lineId, Long userId, Date date, int n) {
        return getFirstReservationsWithLineIdAndChildIdAndDate(lineId, userId, date, PageRequest.of(0, n));
    }
}
