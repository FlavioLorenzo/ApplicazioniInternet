package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AvailabilityPostBody {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("ride_id")
    private Long rideId;

    @JsonProperty("stop_id")
    private Long stopId;
}

