package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AvailabilityPostBody {
    @JsonProperty("userId")
    @NotBlank
    private Long userId;

    @JsonProperty("rideId")
    @NotBlank
    private Long rideId;

    @JsonProperty("stopId")
    @NotBlank
    private Long stopId;
}

