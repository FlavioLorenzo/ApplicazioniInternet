package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Enums.RideBookingStatusConverter;
import com.internet_application.backend.Utils.DateUtils;

import java.io.IOException;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class RideDeserializer extends JsonDeserializer<RideEntity> {
    @Override
    public RideEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_ride").asLong();
        final Date day = DateUtils.dateParser(node.get("date").asText());
        final Boolean direction = node.get("direction").asBoolean();
        final Long id_line = node.get("id_line").asLong();
        final Integer rideBookingStatus = node.get("ride_booking_status").asInt();

        StopEntity latestStop = null;
        if(node.has("latest_stop_id")) {
            Long latestStopId = node.get("latest_stop_id").asLong();

            latestStop = new StopEntity();
            latestStop.setId(latestStopId);
        }

        Date latestStopTime = null;
        if(node.has("latest_stop_time"))
            latestStopTime = DateUtils.timeParser(node.get("latest_stop_time").asText());

        BusLineEntity line = new BusLineEntity();
        line.setId(id_line);

        RideEntity ride = new RideEntity();

        RideBookingStatusConverter rideBookingStatusConverter = new RideBookingStatusConverter();

        ride.setId(id);
        ride.setDate(day);
        ride.setDirection(direction);
        ride.setLine(line);
        ride.setRideBookingStatus(rideBookingStatusConverter.convertToEntityAttribute(rideBookingStatus));
        ride.setLatestStop(latestStop);
        ride.setLatestStopTime(latestStopTime);

        return ride;
    }
}
