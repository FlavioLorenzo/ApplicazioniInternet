package com.internet_application.exercise_3.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RecoverNewPwdPostBody {
    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory")
    @Size(min=8, max=32)
    private String password;

    @JsonProperty("confirm_password")
    @NotBlank(message = "Password is mandatory")
    @Size(min=8, max=32)
    private String confirmPassword;
}
