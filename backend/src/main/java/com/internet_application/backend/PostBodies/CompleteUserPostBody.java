package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CompleteUserPostBody {
    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory")
    @Size(min=8, max=32, message = "Size must be between 6 and 32 characters long")
    private String password;

    @JsonProperty("confirm_password")
    @NotBlank(message = "Confirm password is mandatory")
    @Size(min=8, max=32, message = "Size must be between 6 and 32 characters long")
    private String confirmPassword;

    @Pattern(regexp = "(\\+39)?[0-9]{8,12}")
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is mandatory")
    @Size(min=4, max=255)
    private String phone;

}

