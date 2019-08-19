package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Repositories.BusLineRepository;
import com.internet_application.backend.Repositories.RideRepository;
import com.internet_application.backend.Utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class RideServiceImpl implements RideService {
    @Autowired
    private RideRepository rideRepository;
    @PersistenceContext
    private EntityManager em;

    public List<RideEntity> getNRidesFromDate(Long lineId, String date, Integer n){
        Date d = MiscUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<RideEntity> rides = rideRepository.findTopNRidesWithLineIdAndDate(lineId, d, pageNumber*2);

        return rides;
    }
}
