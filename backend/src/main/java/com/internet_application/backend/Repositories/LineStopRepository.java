package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.LineStopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineStopRepository extends JpaRepository<LineStopEntity, Long> {
    @Query("SELECT ls FROM LineStopEntity ls " +
            "WHERE ls.line.id = ?1 " +
            "AND ls.stop.id = ?2 " +
            "AND ls.direction = ?3")
    List<LineStopEntity> getLineStopsWithLineIdAndStopIdAndDir(Long lineId, Long stopId, Boolean dir);

    @Query("SELECT COUNT(ls2) FROM LineStopEntity ls1, LineStopEntity ls2 " +
            "WHERE ls1.line.id = ?1 " +
            "AND ls2.line.id = ?1 " +
            "AND ls1.stop.id = ?2 " +
            "AND ls2.stop.id = ?3 " +
            "AND ls1.direction = ?4 " +
            "AND ls2.direction = ?4 " +
            "AND ls1.arrivalTime < ls2.arrivalTime")
    List<LineStopEntity> countLineStopsWithLineIdAndStopIdAndDir(Long lineId, Long stopId, Boolean dir);
}
