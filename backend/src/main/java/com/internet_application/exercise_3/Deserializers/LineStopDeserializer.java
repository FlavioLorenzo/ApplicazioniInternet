package com.internet_application.exercise_3.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.exercise_3.Entities.BusLineEntity;
import com.internet_application.exercise_3.Entities.LineStopEntity;
import com.internet_application.exercise_3.Entities.StopEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("Duplicates")
public class LineStopDeserializer extends JsonDeserializer<LineStopEntity> {

    @Override
    public LineStopEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_busline_stop").asLong();
        final Long id_line = node.get("id_line").asLong();
        final Long id_stop = node.get("id_stop").asLong();
        final String arrival_time = node.get("arrival_time").asText();
        final Boolean direction = node.get("direction").asBoolean();

        System.out.println(id + id_line + id_stop + arrival_time + direction);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        BusLineEntity line = new BusLineEntity();
        line.setId(id_line);

        StopEntity stop = new StopEntity();
        stop.setId(id_stop);

        LineStopEntity lineStop = new LineStopEntity();

        try {
            lineStop.setId(id);
            lineStop.setStop(stop);
            lineStop.setLine(line);
            lineStop.setDirection(direction);
            lineStop.setArrivalTime(df.parse(arrival_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lineStop;
    }

}
