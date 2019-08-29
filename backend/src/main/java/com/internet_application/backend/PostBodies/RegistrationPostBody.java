package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationPostBody {
    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "You must provide a valid email")
    @Size(min=4, max=255)
    private String email;

    @JsonProperty("first_name")
    @NotBlank(message = "First name is mandatory")
    @Size(min=4, max=255)
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name is mandatory")
    @Size(min=4, max=255)
    private String lastName;

    @JsonProperty("role")
    private Long roleId;
}
