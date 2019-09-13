package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.internet_application.backend.Utils.DateUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("Duplicates")
public class BusLineInitDeserializer extends JsonDeserializer<BusLineEntity> {
    @Override
    public BusLineEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        // Define the admin
        final Long id_admin = node.get("adminId").asLong();
        UserEntity admin = new UserEntity();
        admin.setId(id_admin);

        Set<LineStopEntity> outwardStops = deserializeLineStops((ArrayNode) node.get("toSchool"));

        Set<LineStopEntity> returnStops = deserializeLineStops((ArrayNode) node.get("fromSchool"));

        BusLineEntity line = new BusLineEntity();

        line.setAdmin(admin);
        line.setOutwordStops(outwardStops);
        line.setReturnStops(returnStops);

        return line;
    }

    private Set<LineStopEntity> deserializeLineStops(ArrayNode lineStops) {
        Set<LineStopEntity> lineStopsSet = new HashSet<>();

        for(JsonNode node: lineStops) {
            final Long id_stop = node.get("id_stop").asLong();
            final Date arrival_time = DateUtils.timeParser(node.get("arrival_time").asText());
            final Boolean direction = node.get("direction").asBoolean();

            StopEntity stop = new StopEntity();
            stop.setId(id_stop);

            LineStopEntity lineStop = new LineStopEntity();

            lineStop.setStop(stop);
            lineStop.setDirection(direction);
            lineStop.setArrivalTime(arrival_time);

            lineStopsSet.add(lineStop);
        }

        return lineStopsSet;
    }
}
