package com.internet_application.backend.PostBodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationPostBody {
    @JsonProperty("id_user")
    public Long id_user;
    @JsonProperty("join_stop")
    public Long join_stop;
    @JsonProperty("leave_stop")
    public Long leave_stop;
    @JsonProperty("direction")
    public Boolean direction;
}
