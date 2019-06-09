package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopRepository extends JpaRepository<StopEntity, Long> {
}
