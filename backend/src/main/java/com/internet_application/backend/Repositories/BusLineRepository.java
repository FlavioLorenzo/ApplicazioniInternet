package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.BusLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusLineRepository extends JpaRepository<BusLineEntity, Long> {
}
