package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChildPostBody {
    @JsonProperty("childId")
    @NotBlank
    private Long userId;

    @JsonProperty("firstName")
    @NotBlank
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank
    private String lastName;

    @JsonProperty("phone")
    @NotBlank
    private String phone;
}
