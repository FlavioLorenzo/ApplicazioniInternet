package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Repositories.BusLineRepository;
import com.internet_application.backend.Repositories.LineStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class LineStopServiceImpl implements LineStopService {
    @Autowired
    private LineStopRepository lineStopRepository;// Using PersistenceContext we don't need to instantiate the EntityManager every time
    @PersistenceContext
    private EntityManager em;

    public LineStopEntity getLineStopWithLineIdAndStopIdAndDir(Long lineId, Long stopId, boolean direction) {
        List<LineStopEntity> lineStopList = lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(
                lineId,
                stopId,
                direction);

        if (lineStopList.size() != 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This stop is not present in the selected line");
        return lineStopList.get(0);
    }

    public Integer countLineStopsAfterProvidedOne(LineStopEntity lineStop) {
        return lineStopRepository.countLineStopsAfterProvidedOne(lineStop.getId(), lineStop.getLine().getId(), lineStop.getDirection());
    }

}
