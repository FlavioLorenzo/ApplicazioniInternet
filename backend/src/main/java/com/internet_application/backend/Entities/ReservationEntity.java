package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @JsonProperty("id_reservation")
    @Column(name="id_reservation")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("id_ride")
    @ManyToOne
    @JoinColumn(name = "id_ride")
    @JsonBackReference("ride_reservation")
    private RideEntity ride;

    @JsonProperty("id_user")
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private UserEntity user;

    @JsonProperty("id_stop")
    @ManyToOne
    @JoinColumn(name = "id_stop")
    private StopEntity stop;

    @JsonProperty("presence")
    @Column
    @Getter
    @Setter
    private boolean presence; // 0 absent, 1 present

    public StopEntity getStop() {
        return stop;
    }

    public void setStop(StopEntity stop) {
        this.stop = stop;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RideEntity getRide() {
        return ride;
    }

    public void setRide(RideEntity ride) {
        this.ride = ride;
    }

    public boolean getPresence() {
        return presence;
    }

    @Override
    public String toString() {
        String result =  "Reservation " + id + ":\n" +
                "\tRide: \n------------" + ride.toString() + "------------\n" +
                "\tUser: \n------------" + user.toString() + "------------\n" +
                "\tStop: \n------------" + stop.toString() + "------------\n" +
                "\tPresent: \n------------" + presence + "------------\n";
        return result;
    }
}

