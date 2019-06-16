package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CheckEmailPostBody {
    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "You must provide a valid email")
    private String email;
}
