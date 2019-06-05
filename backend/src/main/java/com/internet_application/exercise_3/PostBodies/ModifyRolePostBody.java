package com.internet_application.exercise_3.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModifyRolePostBody {
    @JsonProperty("line")
    @NotBlank(message = "New Line is mandatory")
    private String line;

    @JsonProperty("role")
    @NotBlank(message = "New Role is mandatory")
    private String role;
}
