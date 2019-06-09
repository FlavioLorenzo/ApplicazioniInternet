package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Repositories.BusLineRepository;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Repositories.ReservationRepository;
import com.internet_application.backend.Repositories.RideRepository;
import com.internet_application.backend.Serializers.UserSerializer;
import com.internet_application.backend.Utils.MiscUtils;
import com.internet_application.backend.PostBodies.ReservationPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
@Transactional//(isolation = Isolation.SERIALIZABLE)
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private LineStopRepository lineStopRepository;
    @Autowired
    private BusLineRepository busLineRepository;

    @PersistenceContext
    private EntityManager em;

    public JsonNode getAllReservationForLineAndData(Long lineId, String date) {
        Date d = MiscUtils.dateParser(date);

        List<RideEntity> rides = rideRepository.getAllRidesWithLineIdAndDate(lineId, d);

        // I start to create a JSON node to hold the answer
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();

        // For each ride
        rides.forEach((ride) -> {
            JsonNode rideNode = mapper.createObjectNode();

            // Translate the direction into a human comprehensible information and add it to the JsonNode
            ((ObjectNode) rideNode).put("direction", MiscUtils.directionBoolToString(ride.getDirection().booleanValue()));

            List<StopEntity> stops = rideRepository.getAllStopsOfRide(ride.getLine().getId(), ride.getDirection());

            ArrayNode stopNodes = mapper.createArrayNode();
            // For each stop
            stops.forEach((stop) -> {
                JsonNode stopNode = mapper.createObjectNode();
                ((ObjectNode) stopNode).put("Name", stop.getName());

                // Select all users departing from this stop
                List<UserEntity> users = reservationRepository.getAllJoiningUsersByStopIdAndRideId(stop.getId(), ride.getId());

                ArrayNode joinUsers = UserSerializer.serializeUserArrayNodeWithFirstAndLastName(mapper, users);
                ((ObjectNode) stopNode).set("Joining", joinUsers);

                // Select all users leaving from this stop
                users = reservationRepository.getAllLeavingUsersByStopIdAndRideId(stop.getId(), ride.getId());

                ArrayNode leaveUsers = UserSerializer.serializeUserArrayNodeWithFirstAndLastName(mapper, users);
                ((ObjectNode) stopNode).set("Leaving", leaveUsers);

                stopNodes.add(stopNode);
            });
            ((ObjectNode) rideNode).set("Stop", stopNodes);
            rootNode.add(rideNode);

        });

        return rootNode;
    }

    public ReservationEntity addReservation(Long lineId, String date, ReservationPostBody rpb) throws ResponseStatusException  {
        ReservationEntity r = buildReservation(lineId, date, rpb);
        reservationRepository.save(r);

        return r;
    }

    public ReservationEntity updateReservation(Long lineId, String date, Long reservationId, ReservationPostBody rpb) throws ResponseStatusException {
        ReservationEntity r = em.find(ReservationEntity.class, reservationId);
        /*Check if the reservation already exists*/
        if(r == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ReservationEntity builtReservation = buildReservation(lineId, date, rpb);

        r.setRide(builtReservation.getRide());
        r.setJoinStop(builtReservation.getJoinStop());
        r.setLeaveStop(builtReservation.getLeaveStop());
        r.setUser(builtReservation.getUser());
        reservationRepository.save(r);

        return r;
    }

    private ReservationEntity buildReservation(Long lineId, String date, ReservationPostBody rpb) {
        /* Name convention for all the passed variables */
        Date d = MiscUtils.dateParser(date);
        Boolean dir = rpb.direction;
        Long joinStopId = rpb.join_stop;
        Long leaveStopId = rpb.leave_stop;
        Long userId = rpb.id_user;

        /* Check the busline exists */
        if (!busLineRepository.existsById(lineId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        UserEntity user = em.find(UserEntity.class, userId);
        /* Check the user exists */
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        /* Check if a ride is available */
        RideEntity ride;
        List<RideEntity> rdQueryResult = rideRepository.getRidesWithLineIdAndDateAndDirection(lineId, d, dir);

        if (rdQueryResult.size() != 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ride = rdQueryResult.get(0);

        /* Check if the join stop is present on that line */
        LineStopEntity joinStop;

        List<LineStopEntity> lineStopList = lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(lineId, joinStopId, dir);

        if (lineStopList.size() != 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        joinStop = lineStopList.get(0);

        /* Check if the leave stop is present on that line */
        LineStopEntity leaveStop;
        lineStopList =  lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(lineId, leaveStopId, dir);

        if (lineStopList.size() != 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        leaveStop = lineStopList.get(0);

        ReservationEntity r = new ReservationEntity();
        Long id = reservationRepository.getLastId().get(0);
        r.setId(id + 1);
        r.setRide(ride);
        r.setJoinStop(joinStop.getStop());
        r.setLeaveStop(leaveStop.getStop());
        r.setUser(user);

        return r;
    }

    public ReservationEntity getReservation(Long lineId, String date, Long reservationId) {
        Date d = MiscUtils.dateParser(date);

        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);

        // Check if there was a reservation with the specified ID
        if (reservation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // Since we have tons of useless data, we can also do some other completely unnecessary test to check if
        // the one who populated the DB (or made the request) was drunk
        if(!(reservation.getRide().getLine().getId().equals(lineId) && reservation.getRide().getDate().equals(d)))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return reservation;
    }

    public void deleteReservation(Long lineId, String date, Long reservationId){
        ReservationEntity res = getReservation(lineId, date, reservationId);
        em.remove(em.merge(res));
    }
}
