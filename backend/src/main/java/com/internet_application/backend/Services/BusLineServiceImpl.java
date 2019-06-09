package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Repositories.BusLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
public class BusLineServiceImpl implements BusLineService {
    @Autowired
    private BusLineRepository busLineRepository;// Using PersistenceContext we don't need to instantiate the EntityManager every time
    @PersistenceContext
    private EntityManager em;

    public List<BusLineEntity> getAllBusLines() {
        return busLineRepository.findAll();
    }

    public List<List<LineStopEntity>> getBusLineWithName(Long lineId) {
        TypedQuery<LineStopEntity> query = em.createNamedQuery("LineStopEntity.findAllWithIdLineAndDirection",
                LineStopEntity.class);
        query.setParameter("id", lineId);
        query.setParameter("dir", false);
        List<LineStopEntity> resForward = query.getResultList();

        for(LineStopEntity ls:resForward)
            System.out.println(ls);

        query.setParameter("dir", true);
        List<LineStopEntity> resBackward = query.getResultList();
        List<List<LineStopEntity>> res = new ArrayList<>();
        res.add(resForward);
        res.add(resBackward);
        return res;
    }
}
