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
import com.internet_application.backend.Utils.DateUtils;
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
    @Autowired
    private NotificationService notificationService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReservationEntity getReservationById(Long reservationId)
            throws ResponseStatusException {
        ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return reservation;
    }

    public JsonNode getAllReservationForLineAndData(Long lineId, String date) {
        Date d = DateUtils.dateParser(date);

        List<RideEntity> rides = rideRepository.getAllRidesWithLineIdAndDate(lineId, d);

        // I start to create a JSON node to hold the answer
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootNode = mapper.createArrayNode();

        // For each ride
        rides.forEach((ride) -> {
            JsonNode rideNode = mapper.createObjectNode();

            ((ObjectNode) rideNode).put("id", ride.getId().longValue());
            ((ObjectNode) rideNode).put("date", DateUtils.dateToString(d));

            // Translate the direction into a human comprehensible information and add it to the JsonNode
            ((ObjectNode) rideNode).put("direction", MiscUtils.directionBoolToString(ride.getDirection().booleanValue()));

            List<StopEntity> stops = rideRepository.getAllStopsOfRide(ride.getLine().getId(), ride.getDirection());

            ArrayNode stopNodes = mapper.createArrayNode();
            // For each stop
            stops.forEach((stop) -> {
                JsonNode stopNode = mapper.createObjectNode();
                List<ChildEntity> children;

                // Set the id and name of the stop
                ((ObjectNode) stopNode).put("id", stop.getId().longValue());
                ((ObjectNode) stopNode).put("name", stop.getName());

                // Set the arrivalTime to the current stop
                List<LineStopEntity> lineStopList = lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(ride.getLine().getId(), stop.getId(), ride.getDirection());
                if (lineStopList.size() != 1)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                ((ObjectNode) stopNode).put("arrivaltime", lineStopList.get(0).getArrivalTime().toString());

                // Set the passengers attribute. Since we only have one property for both joining and leaving
                // children, the meaning of this attribute depends on the direction of the ride (forward - joining children /
                // backward - leaving children)
                children = reservationRepository.getAllChildrenByStopIdAndRideId(stop.getId(), ride.getId());

                // Build a structure for each child that details his first name and whether or not the child is present
                ArrayNode childArray = mapper.createArrayNode();
                children.forEach((child) -> {
                    JsonNode childNode = mapper.createObjectNode();
                    ((ObjectNode) childNode).put("childId", child.getId());
                    ((ObjectNode) childNode).put("firstName", child.getFirstName());
                    ((ObjectNode) childNode).put("lastName", child.getLastName());
                    ((ObjectNode) childNode).put("picked",
                            reservationRepository.getPresenceByChildIdAndRide(child.getId(), ride.getId()));
                    ((ObjectNode) childNode).put("reservationId",
                            reservationRepository.getReservationsByChildIdAndRideId(child.getId(), ride.getId()).get(0).getId());
                    childArray.add(childNode);
                });
                ((ObjectNode) stopNode).set("passengers", childArray);

                stopNodes.add(stopNode);
            });
            ((ObjectNode) rideNode).set("stopList", stopNodes);
            rootNode.add(rideNode);

        });

        return rootNode;
    }

    public List<ReservationEntity> getNReservationsByChildFromDate(Long lineId, Long childId, String date, Integer n) {
        Date d = DateUtils.dateParser(date);

        int pageNumber = n.intValue();

        List<ReservationEntity> reservations =
                reservationRepository.getFirstNReservationsWithLineIdAndChildIdAndDate(lineId, childId, d, pageNumber * 2);

        return reservations;
    }

    public ReservationEntity addReservation(Long lineId, String date, ReservationPostBody rpb) throws ResponseStatusException {

        // Check if a reservation already exists for this child and time
        List<ReservationEntity> reservations = reservationRepository.getReservationsByChildIdAndDateAndDirection(
                        rpb.id_child,
                        DateUtils.dateParser(date),
                        rpb.direction);

        if(reservations.size() > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A reservation for this child and time already exists");

        ReservationEntity r = buildReservation(lineId, date, rpb, false);

        r = reservationRepository.save(r);

        RideEntity ride = r.getRide();

        for(AvailabilityEntity availability: ride.getAvailabilities()) {
            if(availability.getShiftStatus().getCode() == 3) {
                ChildEntity child = r.getChild();

                String direction = ride.getDirection() ? "return ride" : "ride to school";

                notificationService.createNotification(
                        availability.getUser().getId(),
                        "The child " + child.getFirstName() + " " + child.getLastName() + " " +
                                "made a reservation for the " + direction + " of line " +
                                ride.getLine().getName() + " scheduled for the day " +
                                DateUtils.dateToString(ride.getDate()),
                        ""
                );
            }
        }

        return r;
    }

    public ReservationEntity updateReservation(Long lineId,
                                               String date,
                                               Long reservationId,
                                               ReservationPostBody rpb,
                                               boolean isAdmin) throws ResponseStatusException {
        ReservationEntity r = em.find(ReservationEntity.class, reservationId);

        /*Check if the reservation already exists*/
        if (r == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ReservationEntity builtReservation = buildReservation(lineId, date, rpb, isAdmin);

        // If the companion is trying to update the child presence, check that the ride is already started
        if(r.getPresence() != builtReservation.getPresence() &&
                r.getRide().getRideBookingStatus() != RideBookingStatus.IN_PROGRESS)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "To set the presence of a child the ride needs to be started");

        r.setRide(builtReservation.getRide());
        r.setStop(builtReservation.getStop());
        r.setChild(builtReservation.getChild());
        r.setPresence(builtReservation.getPresence());
        reservationRepository.save(r);

        if(r.getPresence() == true) {
            ChildEntity child = r.getChild();

            notificationService.createNotification(
                    child.getParent().getId(),
                    "Your child " + child.getFirstName() + " " + child.getLastName() + " " +
                            "has been marked as present.",
                    ""
            );
        }

        return r;
    }

    private ReservationEntity buildReservation(Long lineId, String date, ReservationPostBody rpb, boolean isAdmin) {
        /* Name convention for all the passed variables */
        Date d = DateUtils.dateParser(date);
        Boolean dir = rpb.direction;
        Long stopId = rpb.id_stop;
        Long childId = rpb.id_child;
        Boolean presence = (rpb.presence != null) ? rpb.presence : false;

        /* Check the busline exists */
        if (!busLineRepository.existsById(lineId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ChildEntity child = em.find(ChildEntity.class, childId);

        /* Check the child exists */
        if (child == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        /* Check if a ride is available */
        RideEntity ride;
        List<RideEntity> rdQueryResult = rideRepository.getRidesWithLineIdAndDateAndDirection(lineId, d, dir);

        if (rdQueryResult.size() != 1 )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ride = rdQueryResult.get(0);

        /* Check if the selected ride is not terminated yet */
        rideService.isRideEnded(ride);

        /* Check if the stop is present on that line */
        LineStopEntity stop;

        // If the stopId is less than 0, than we request the creation of a reservation for the first available stop
        if(stopId < 0) {
            if(ride.getLatestStop() != null)
                stop = lineStopService.getFirstLineStopAvailableWithLineIdAndDir(lineId, dir, ride.getLatestStop().getId());
            else{
                stop = lineStopService.getFirstLineStopWithLineIdAndDir(lineId, dir);
            }
        } else {
            stop = lineStopService.getLineStopWithLineIdAndStopIdAndDir(lineId, stopId, dir);
        }

        if( !isAdmin ) {
            /* Check if the reservation is valid (i.e. the ride to which the reservation refers is not terminated or has
            already passed through the desired stop ) */
            rideService.isRidePassedOrEnded(stop, ride);
        }

        ReservationEntity r = new ReservationEntity();
        r.setRide(ride);
        r.setStop(stop.getStop());
        r.setChild(child);
        r.setPresence(presence);
        return r;
    }

    public ReservationEntity getReservation(Long lineId, String date, Long reservationId) {
        Date d = DateUtils.dateParser(date);

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

        ChildEntity child = res.getChild();
        RideEntity ride = res.getRide();

        em.remove(em.merge(res));

        for(AvailabilityEntity availability: ride.getAvailabilities()) {
            if(availability.getShiftStatus().getCode() == 3) {
                String direction = ride.getDirection() ? "return ride" : "ride to school";

                notificationService.createNotification(
                        availability.getUser().getId(),
                        "The child " + child.getFirstName() + " " + child.getLastName() + " " +
                                "deleted his reservation for the " + direction + " of line " +
                                ride.getLine().getName() + " scheduled for the day " +
                                DateUtils.dateToString(ride.getDate()),
                        ""
                );
            }
        }
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
