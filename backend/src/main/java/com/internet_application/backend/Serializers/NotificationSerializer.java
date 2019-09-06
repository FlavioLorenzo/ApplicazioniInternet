package com.internet_application.backend.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.internet_application.backend.Entities.NotificationEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Utils.DateUtils;

import java.io.IOException;

public class NotificationSerializer extends StdSerializer<NotificationEntity> {

    public NotificationSerializer() {
        this(null);
    }

    public NotificationSerializer(Class<NotificationEntity> t) {
        super(t);
    }

    @Override
    public void serialize(
            NotificationEntity value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("notificationId", value.getId());
        jgen.writeNumberField("userId", value.getUser().getId());
        jgen.writeStringField("date", DateUtils.dateToString(value.getDate()));
        jgen.writeStringField("message", value.getMessage());
        jgen.writeStringField("link", value.getLink());
        jgen.writeBooleanField("viewed", value.getViewed());

        jgen.writeEndObject();
    }
}
