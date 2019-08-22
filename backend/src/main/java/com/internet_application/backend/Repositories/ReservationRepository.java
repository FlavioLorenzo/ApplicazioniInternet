package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT u FROM UserEntity u, ReservationEntity r WHERE u.id = r.user.id AND r.stop.id = ?1 AND r.ride.id = ?2")
    List<UserEntity> getAllUsersByStopIdAndRideId(Long stopId, Long rideId);

    /*
    @Query("SELECT u FROM UserEntity u, ReservationEntity r WHERE u.id = r.user.id AND r.leaveStop.id = ?1 AND r.ride.id = ?2")
    List<UserEntity> getAllLeavingUsersByStopIdAndRideId(Long stopId, Long rideId);
    */

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.ride.line.id = ?1 " +
            "AND r.user.id = ?2 " +
            "AND r.ride.date >= ?3 " +
            "ORDER BY r.ride.date ASC")
    List<ReservationEntity> getFirstReservationsWithLineIdAndUserIdAndDate(Long lineId, Long userId, Date date, Pageable pageable);

    default List<ReservationEntity> getFirstNReservationsWithLineIdAndUserIdAndDate(Long lineId, Long userId, Date date, int n) {
        return getFirstReservationsWithLineIdAndUserIdAndDate(lineId, userId, date, PageRequest.of(0, n));
    }

    @Query("SELECT r.id from ReservationEntity r ORDER BY r.id DESC")
    List<Long> getLastId();

    @Query("SELECT r.presence FROM ReservationEntity r WHERE r.user.id = ?1 AND r.ride.id = ?2")
    Boolean getPresenceByUserIdAndRide(Long userId, Long rideId);

    @Query("SELECT r FROM ReservationEntity r WHERE r.user.id = ?1 AND r.ride.id = ?2")
    List<ReservationEntity> getReservationsByUserIdAndRideId(Long userId, Long rideId);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.user.id = ?1 " +
            "AND r.ride.line.id = ?2 " +
            "AND r.ride.date = ?3 " +
            "AND r.ride.direction = ?4")
    List<ReservationEntity> getReservationsByUserIdAndLineIdAndDateAndDirection(Long userId, Long lineId, Date date, Boolean direction);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.user.id = ?1 " +
            "AND r.ride.date = ?2 " +
            "AND r.ride.direction = ?3")
    List<ReservationEntity> getReservationsByUserIdAndDateAndDirection(Long userId, Date date, Boolean direction);
}
