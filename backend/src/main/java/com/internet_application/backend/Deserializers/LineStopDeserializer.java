package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Utils.DateUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class LineStopDeserializer extends JsonDeserializer<LineStopEntity> {

    @Override
    public LineStopEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_busline_stop").asLong();
        final Long id_line = node.get("id_line").asLong();
        final Long id_stop = node.get("id_stop").asLong();
        final Date arrival_time = DateUtils.timeParser(node.get("arrival_time").asText());
        final Boolean direction = node.get("direction").asBoolean();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        BusLineEntity line = new BusLineEntity();
        line.setId(id_line);

        StopEntity stop = new StopEntity();
        stop.setId(id_stop);

        LineStopEntity lineStop = new LineStopEntity();

        lineStop.setId(id);
        lineStop.setStop(stop);
        lineStop.setLine(line);
        lineStop.setDirection(direction);
        lineStop.setArrivalTime(arrival_time);

        return lineStop;
    }

}
