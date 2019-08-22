package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationPostBody {
    @JsonProperty("id_child")
    public Long id_child;
    @JsonProperty("id_stop")
    public Long id_stop;
    @JsonProperty("direction")
    public Boolean direction;
    @JsonProperty("presence")
    public Boolean presence;
}
