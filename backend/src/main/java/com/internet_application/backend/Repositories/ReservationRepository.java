package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.ReservationEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT c FROM ChildEntity c, ReservationEntity r WHERE c.id = r.child.id AND r.stop.id = ?1 AND r.ride.id = ?2")
    List<ChildEntity> getAllChildrenByStopIdAndRideId(Long stopId, Long rideId);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.ride.line.id = ?1 " +
            "AND r.child.id = ?2 " +
            "AND r.ride.date >= ?3 " +
            "ORDER BY r.ride.date ASC")
    List<ReservationEntity> getFirstReservationsWithLineIdAndChildIdAndDate(Long lineId, Long childId, Date date, Pageable pageable);

    default List<ReservationEntity> getFirstNReservationsWithLineIdAndChildIdAndDate(Long lineId, Long childId, Date date, int n) {
        return getFirstReservationsWithLineIdAndChildIdAndDate(lineId, childId, date, PageRequest.of(0, n));
    }

    @Query("SELECT r.id from ReservationEntity r ORDER BY r.id DESC")
    List<Long> getLastId();

    @Query("SELECT r.presence FROM ReservationEntity r WHERE r.child.id = ?1 AND r.ride.id = ?2")
    Boolean getPresenceByChildIdAndRide(Long childId, Long rideId);

    @Query("SELECT r FROM ReservationEntity r WHERE r.child.id = ?1 AND r.ride.id = ?2")
    List<ReservationEntity> getReservationsByChildIdAndRideId(Long childId, Long rideId);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.child.id = ?1 " +
            "AND r.ride.line.id = ?2 " +
            "AND r.ride.date = ?3 " +
            "AND r.ride.direction = ?4")
    List<ReservationEntity> getReservationsByChildIdAndLineIdAndDateAndDirection(Long childId, Long lineId, Date date, Boolean direction);

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.child.id = ?1 " +
            "AND r.ride.date = ?2 " +
            "AND r.ride.direction = ?3")
    List<ReservationEntity> getReservationsByChildIdAndDateAndDirection(Long childId, Date date, Boolean direction);
}
