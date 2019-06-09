package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginPostBody {
    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "You must provide a valid email")
    @Size(min=4, max=255)
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory")
    @Size(min=8, max=32, message = "Size must be between 6 and 32 characters long")
    private String password;
}
