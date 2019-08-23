package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AvailabilityPostBody {
    @JsonProperty("user_id")
    @NotBlank
    private Long userId;

    @JsonProperty("ride_id")
    @NotBlank
    private Long rideId;

    @JsonProperty("stop_id")
    @NotBlank
    private Long stopId;
}

