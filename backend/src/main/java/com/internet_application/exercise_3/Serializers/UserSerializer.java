package com.internet_application.exercise_3.Serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internet_application.exercise_3.Entities.UserEntity;

import java.util.List;

@SuppressWarnings("Duplicates")
public class UserSerializer {
    public static ArrayNode serializeUserArrayNodeWithFirstAndLastName(ObjectMapper mapper, List<UserEntity> users) {
        ArrayNode userArray = mapper.createArrayNode();
        users.forEach((user) -> {
            JsonNode userNode = mapper.createObjectNode();
            ((ObjectNode) userNode).put("First Name", user.getFirstName());
            ((ObjectNode) userNode).put("Last Name", user.getLastName());
            userArray.add(userNode);
        });

        return userArray;
    }
}
