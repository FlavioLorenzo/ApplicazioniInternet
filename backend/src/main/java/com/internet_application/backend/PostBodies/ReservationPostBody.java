package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationPostBody {
    @JsonProperty("id_user")
    public Long id_user;
    @JsonProperty("id_stop")
    public Long id_stop;
    @JsonProperty("direction")
    public Boolean direction;
    @JsonProperty("presence")
    public Boolean presence;
}
