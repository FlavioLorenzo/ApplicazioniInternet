package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChildPostBody {
    @JsonProperty("user_id")
    @NotBlank
    private Long userId;

    @JsonProperty("first_name")
    @NotBlank
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    private String lastName;

    @JsonProperty("phone")
    @NotBlank
    private String phone;
}
