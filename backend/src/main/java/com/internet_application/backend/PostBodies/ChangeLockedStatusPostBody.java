package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeLockedStatusPostBody {
    @JsonProperty("locked")
    @NotBlank(message = "Locked is mandatory")
    private Boolean locked;
}
