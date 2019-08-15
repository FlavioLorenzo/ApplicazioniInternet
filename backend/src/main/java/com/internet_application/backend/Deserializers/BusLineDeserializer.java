package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.BusLineEntity;
import com.internet_application.backend.Entities.RideEntity;
import com.internet_application.backend.Entities.UserEntity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("Duplicates")
public class BusLineDeserializer extends JsonDeserializer<BusLineEntity> {
    @Override
    public BusLineEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_line").asLong();
        final String name = node.get("name").asText();
        final Long id_admin = node.get("id_admin").asLong();

        UserEntity admin = new UserEntity();
        admin.setId(id_admin);
        BusLineEntity line = new BusLineEntity();

        line.setId(id);
        line.setName(name);
        line.setAdmin(admin);

        return line;
    }
}
