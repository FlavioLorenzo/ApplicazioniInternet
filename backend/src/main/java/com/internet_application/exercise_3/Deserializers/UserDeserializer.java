package com.internet_application.exercise_3.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.exercise_3.Entities.RoleEntity;
import com.internet_application.exercise_3.Entities.UserEntity;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class UserDeserializer extends JsonDeserializer<UserEntity> {
    @Override
    public UserEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_user").asLong();
        final String first_name = node.get("first_name").asText();
        final String last_name = node.get("last_name").asText();
        final String phone = node.get("phone").asText();
        final String email = node.get("email").asText();
        final String password = node.get("password").asText();

        UserEntity user = new UserEntity();

        user.setId(id);
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
