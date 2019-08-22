package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internet_application.backend.Entities.*;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Repositories.BusLineRepository;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Repositories.ReservationRepository;
import com.internet_application.backend.Repositories.RideRepository;
import com.internet_application.backend.Utils.MiscUtils;
import com.internet_application.backend.PostBodies.ReservationPostBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Service
@Transactional //(isolation = Isolation.SERIALIZABLE)
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private LineStopRepository lineStopRepository;
    @Autowired
    private BusLineRepository busLineRepository;
    @Autowired
    private RideService rideService;
    @Autowired
    private LineStopService lineStopService;

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

            ((ObjectNode) rideNode).put("id", ride.getId().longValue());
            ((ObjectNode) rideNode).put("date", MiscUtils.dateToString(d));

            // Translate the direction into a human comprehensible information and add it to the JsonNode
            ((ObjectNode) rideNode).put("direction", MiscUtils.directionBoolToString(ride.getDirection().booleanValue()));

            List<StopEntity> stops = rideRepository.getAllStopsOfRide(ride.getLine().getId(), ride.getDirection());

            ArrayNode stopNodes = mapper.createArrayNode();
            // For each stop
            stops.forEach((stop) -> {
                JsonNode stopNode = mapper.createObjectNode();
                List<UserEntity> users;

                // Set the id and name of the stop
                ((ObjectNode) stopNode).put("id", stop.getId().longValue());
                ((ObjectNode) stopNode).put("name", stop.getName());

                // Set the arrivalTime to the current stop
                List<LineStopEntity> lineStopList = lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(ride.getLine().getId(), stop.getId(), ride.getDirection());
                if (lineStopList.size() != 1)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                ((ObjectNode) stopNode).put("arrivaltime", lineStopList.get(0).getArrivalTime().toString());

                // Set the passengers attribute. Since we only have one property for both joining and leaving
                // users, the meaning of this attribute depends on the direction of the ride (forward - joining users /
                // backward - leaving users)
                users = reservationRepository.getAllUsersByStopIdAndRideId(stop.getId(), ride.getId());

                /*
                // We decide which list to use (joining/leaving users) based on the direction (Forth/Back)
                if(ride.getDirection().booleanValue() == false) {
                    // Select all users departing from this stop
                    users = reservationRepository.getAllJoiningUsersByStopIdAndRideId(stop.getId(), ride.getId());
                } else {
                    // Select all users leaving from this stop
                    users = reservationRepository.getAllLeavingUsersByStopIdAndRideId(stop.getId(), ride.getId());
                }
                */

                // Build a structure for each user that details his first name and whether or not the user is present
                ArrayNode userArray = mapper.createArrayNode();
                users.forEach((user) -> {
                    JsonNode userNode = mapper.createObjectNode();
                    ((ObjectNode) userNode).put("userId", user.getId());
                    ((ObjectNode) userNode).put("username", user.getFirstName());
                    ((ObjectNode) userNode).put("picked",
                            reservationRepository.getPresenceByUserIdAndRide(user.getId(), ride.getId()));
                    ((ObjectNode) userNode).put("reservationId",
                            reservationRepository.getReservationEntitiesByUserIdAndRideId(user.getId(), ride.getId()).get(0).getId());
                    userArray.add(userNode);
                });
                ((ObjectNode) stopNode).set("passengers", userArray);

                stopNodes.add(stopNode);
            });
            ((ObjectNode) rideNode).set("stopList", stopNodes);
            rootNode.add(rideNode);

        });

        return rootNode;
    }

    public List<ReservationEntity> getNReservationsByUserFromDate(Long lineId, Long userId, String date, Integer n) {
        Date d = MiscUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<ReservationEntity> reservations =
                reservationRepository.getFirstNReservationsWithLineIdAndUserIdAndDate(lineId, userId, d, pageNumber * 2);

        return reservations;
    }

    public ReservationEntity addReservation(Long lineId, String date, ReservationPostBody rpb) throws ResponseStatusException {

        // Check if a reservation already exists for this user and ride
        List<ReservationEntity> reservations = reservationRepository.getReservationEntitiesByUserIdAndLineIdAndDateAndDirection(
                        rpb.id_user,
                        lineId,
                        MiscUtils.dateParser(date),
                        rpb.direction);

        if(reservations.size() > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reservation for this user and ride already exists");

        ReservationEntity r = buildReservation(lineId, date, rpb);

        r = reservationRepository.save(r);

        return r;
    }

    public ReservationEntity updateReservation(Long lineId, String date, Long reservationId, ReservationPostBody rpb) throws ResponseStatusException {
        ReservationEntity r = em.find(ReservationEntity.class, reservationId);

        /*Check if the reservation already exists*/
        if (r == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ReservationEntity builtReservation = buildReservation(lineId, date, rpb);

        r.setRide(builtReservation.getRide());
        r.setStop(builtReservation.getStop());
        r.setUser(builtReservation.getUser());
        r.setPresence(builtReservation.getPresence());
        reservationRepository.save(r);

        return r;
    }

    private ReservationEntity buildReservation(Long lineId, String date, ReservationPostBody rpb) {
        /* Name convention for all the passed variables */
        Date d = MiscUtils.dateParser(date);
        Boolean dir = rpb.direction;
        Long stopId = rpb.id_stop;
        Long userId = rpb.id_user;
        Boolean presence = (rpb.presence != null) ? rpb.presence : false;

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

        if (rdQueryResult.size() != 1 )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ride = rdQueryResult.get(0);

        /* Check if the stop is present on that line */
        LineStopEntity stop = lineStopService.getLineStopWithLineIdAndStopIdAndDir(lineId, stopId, dir);

        /* Check if the reservation is valid (i.e. the ride to which the reservation refers is not terminated or has
           already passed through the desired stop ) */
        rideService.isRidePassedOrEnded(stop, ride);

        ReservationEntity r = new ReservationEntity();
        Long id = reservationRepository.getLastId().get(0);
        r.setId(id + 1);
        r.setRide(ride);
        r.setStop(stop.getStop());
        r.setUser(user);
        r.setPresence(presence);
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
        if (!(reservation.getRide().getLine().getId().equals(lineId) && reservation.getRide().getDate().equals(d)))
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        return reservation;
    }

    public void deleteReservation(Long lineId, String date, Long reservationId) {
        ReservationEntity res = getReservation(lineId, date, reservationId);

        isReservationValid(res);

        em.remove(em.merge(res));
    }

    /**
     * Check if a reservation is still valid
     * @param res The reservation to be checked for validity
     * @return True - If the reservation is still valid. If not, it will throw an exception.
     */
    private boolean isReservationValid(ReservationEntity res) {
        RideEntity ride = res.getRide();

        LineStopEntity lineStop = lineStopService.getLineStopWithLineIdAndStopIdAndDir(
            ride.getLine().getId(),
            res.getStop().getId(),
            ride.getDirection()
        );

        return rideService.isRidePassedOrEnded(lineStop, ride);
    }
}
