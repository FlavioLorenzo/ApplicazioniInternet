package com.internet_application.backend.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Utils.DateUtils;

import java.io.IOException;

public class RideSerializer extends StdSerializer<RideEntity> {

    public RideSerializer() {
        this(null);
    }

    public RideSerializer(Class<RideEntity> t) {
        super(t);
    }

    @Override
    public void serialize(
            RideEntity value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("date", DateUtils.dateToString(value.getDate()));
        jgen.writeBooleanField("direction", value.getDirection());
        jgen.writeNumberField("lineId", value.getLine().getId());
        jgen.writeStringField("lineName", value.getLine().getName());
        jgen.writeStringField("rideBookingStatus", value.getRideBookingStatus().getDescription());
        jgen.writeBooleanField("locked", value.getLocked());

        if(value.getLatestStop() != null)
            jgen.writeStringField("latestStop", value.getLatestStop().getName());
        if(value.getLatestStopTime() != null)
            jgen.writeStringField("latestStopTime", DateUtils.timeToString(value.getLatestStopTime()));
        jgen.writeEndObject();
    }
}
