package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.RideEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("Duplicates")
public class RideDeserializer extends JsonDeserializer<RideEntity> {
    @Override
    public RideEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_ride").asLong();
        final String day = node.get("date").asText();
        final Boolean direction = node.get("direction").asBoolean();
        final Long id_line = node.get("id_line").asLong();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        BusLineEntity line = new BusLineEntity();
        line.setId(id_line);
        RideEntity ride = new RideEntity();

        try {
            ride.setId(id);
            ride.setDate(df.parse(day));
            ride.setDirection(direction);
            ride.setLine(line);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ride;
    }
}
