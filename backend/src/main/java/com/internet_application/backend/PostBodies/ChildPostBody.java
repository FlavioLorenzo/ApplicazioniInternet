package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ChildPostBody {
    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("firstName")
    @NotBlank
    private String firstName;

    @JsonProperty("lastName")
    @NotBlank
    private String lastName;

    @Pattern(regexp = "(\\+39)?[0-9]{8,12}")
    @JsonProperty("phone")
    private String phone;
}
