package com.internet_application.backend.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Utils.DateUtils;

import java.io.IOException;

public class LineStopSerializer extends StdSerializer<LineStopEntity> {

    public LineStopSerializer() {
        this(null);
    }

    public LineStopSerializer(Class<LineStopEntity> t) {
        super(t);
    }

    @Override
    public void serialize(
            LineStopEntity value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeNumberField("lineId", value.getLine().getId());
        jgen.writeStringField("lineName", value.getLine().getName());
        jgen.writeNumberField("stopId", value.getStop().getId());
        jgen.writeStringField("stopName", value.getStop().getName());
        jgen.writeBooleanField("direction", value.getDirection());
        jgen.writeStringField("arrivalTime", DateUtils.timeToString(value.getArrivalTime()));
        jgen.writeEndObject();
    }
}
