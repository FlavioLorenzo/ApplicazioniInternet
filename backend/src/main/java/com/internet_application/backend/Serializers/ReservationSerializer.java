package com.internet_application.backend.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.internet_application.backend.Entities.LineStopEntity;
import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Repositories.LineStopRepository;
import com.internet_application.backend.Utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public class ReservationSerializer extends StdSerializer<ReservationEntity> {

    @Autowired
    LineStopRepository lineStopRepository;

    public ReservationSerializer() {
        this(null);
    }

    public ReservationSerializer(Class<ReservationEntity> t) {
        super(t);
    }

    @Override
    public void serialize(
            ReservationEntity value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeNumberField("rideId", value.getRide().getId());
        jgen.writeNumberField("childId", value.getChild().getId());
        jgen.writeNumberField("stopId", value.getStop().getId());
        jgen.writeStringField("stopName", value.getStop().getName());
        jgen.writeBooleanField("presence", value.getPresence());
        Date date = lineStopRepository.getLineStopsWithLineIdAndStopIdAndDir(
                value.getRide().getLine().getId(),
                value.getStop().getId(),
                value.getRide().getDirection()
        ).get(0).getArrivalTime();
        jgen.writeStringField("arrivalTime", DateUtils.timeToString(date));

        jgen.writeEndObject();
    }
}
