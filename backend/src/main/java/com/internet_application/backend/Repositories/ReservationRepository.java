package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT u FROM UserEntity u, ReservationEntity r WHERE u.id = r.user.id AND r.joinStop.id = ?1 AND r.ride.id = ?2")
    List<UserEntity> getAllJoiningUsersByStopIdAndRideId(Long stopId, Long rideId);

    @Query("SELECT u FROM UserEntity u, ReservationEntity r WHERE u.id = r.user.id AND r.leaveStop.id = ?1 AND r.ride.id = ?2")
    List<UserEntity> getAllLeavingUsersByStopIdAndRideId(Long stopId, Long rideId);

    @Query("SELECT r.id from ReservationEntity r ORDER BY r.id DESC")
    List<Long> getLastId();

    @Query("SELECT r.presence FROM ReservationEntity r WHERE r.user.id = ?1 AND r.ride.id = ?2")
    Boolean getPresenceByUserIdAndRide(Long userId, Long rideId);
}
