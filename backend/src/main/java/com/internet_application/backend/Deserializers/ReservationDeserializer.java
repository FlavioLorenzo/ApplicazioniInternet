package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.*;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class ReservationDeserializer extends JsonDeserializer<ReservationEntity> {

    @Override
    public ReservationEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_reservation").asLong();
        final Long id_ride = node.get("id_ride").asLong();
        final Long id_child = node.get("id_child").asLong();
        final Long id_stop = node.get("id_stop").asLong();

        RideEntity ride = new RideEntity();
        ride.setId(id_ride);

        ChildEntity child = new ChildEntity();
        child.setId(id_child);

        StopEntity stop = new StopEntity();
        stop.setId(id_stop);

        ReservationEntity reservation = new ReservationEntity();
        reservation.setId(id);
        reservation.setRide(ride);
        reservation.setChild(child);
        reservation.setStop(stop);

        return reservation;
    }
}
