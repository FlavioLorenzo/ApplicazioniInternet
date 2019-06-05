package com.internet_application.exercise_3.Repositories;

import com.internet_application.exercise_3.Entities.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {
}
