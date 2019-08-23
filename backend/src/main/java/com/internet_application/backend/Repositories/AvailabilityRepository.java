package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    @Query("SELECT av FROM Availability av " +
            "WHERE av.ride.id = ?1 ")
    List<Availability> getAllAvailabilitiesForRideWithId(Long rideId);

    @Query("SELECT av FROM Availability av " +
            "WHERE av.ride.id = ?1 " +
            "AND av.user.id = ?2")
    Availability findAvailabilityByRideAndUser(Long rideId, Long userId);
}
