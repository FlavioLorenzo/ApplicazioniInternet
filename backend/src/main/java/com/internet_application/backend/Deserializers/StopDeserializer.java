package com.internet_application.backend.Deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.internet_application.backend.Entities.StopEntity;
import com.internet_application.backend.Entities.UserEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class StopDeserializer extends JsonDeserializer<StopEntity> {
    @Override
    public StopEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        GeometryFactory geometryFactory = new GeometryFactory();

        ObjectCodec oc = jp.getCodec();

        JsonNode node = oc.readTree(jp);

        final Long id = node.get("id_stop").asLong();
        final String name = node.get("name").asText();
        final JsonNode gpsNode = node.get("gps");
        final Double lat, lon;

        lon = gpsNode.get(0).asDouble();
        lat = gpsNode.get(1).asDouble();

        Point gps = geometryFactory.createPoint(new Coordinate(lon,lat));

        StopEntity stop = new StopEntity();

        stop.setId(id);
        stop.setName(name);
        stop.setGps(gps);

        return stop;
    }
}
