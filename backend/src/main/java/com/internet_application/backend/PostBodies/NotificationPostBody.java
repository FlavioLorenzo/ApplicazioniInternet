package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
public class NotificationPostBody {
    @JsonProperty("user_id")
    @NotBlank
    private Long userId;

    @JsonProperty("message")
    @NotBlank
    private String message;

    @JsonProperty("link")
    @URL
    @NotBlank
    private String link;

    @JsonProperty("viewed")
    @NotBlank
    private Boolean viewed;
}
