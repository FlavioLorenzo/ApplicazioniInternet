package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.UserEntity;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class ChildDeserializer extends JsonDeserializer<ChildEntity> {
    @Override
    public ChildEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_child").asLong();
        final String first_name = node.get("first_name").asText();
        final String last_name = node.get("last_name").asText();
        final String phone = node.get("phone").asText();
        final Long parent_id = node.get("id_parent").asLong();

        UserEntity parent = new UserEntity();
        parent.setId(parent_id);

        ChildEntity child = new ChildEntity();

        child.setId(id);
        child.setFirstName(first_name);
        child.setLastName(last_name);
        child.setPhone(phone);
        child.setParent(parent);

        return child;
    }
}
