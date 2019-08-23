package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "availability")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_availability")
    @Column(name="id_availability")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("ride")
    @ManyToOne
    @JoinColumn(name = "id_ride")
    @JsonBackReference("ride_availability")
    private RideEntity ride;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonBackReference
    private UserEntity user;

    @JsonProperty("stop")
    @ManyToOne
    @JoinColumn(name = "id_stop", referencedColumnName = "id_stop")
    private StopEntity stop;

    // Whether the admin confirmed the shift
    @JsonProperty("confirmed")
    @Column
    @Getter
    @Setter
    private Boolean confirmed; // 0 not confirmed, 1 confirmed

    // Whether the user viewed the confirmation
    @JsonProperty("viewed")
    @Column
    @Getter
    @Setter
    private Boolean viewed; // 0 not viewed, 1 viewed

    @JsonProperty("locked")
    @Column
    @Getter
    @Setter
    private Boolean locked; // 0 unlocked, 1 locked


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

    @Override
    public String toString() {
        String result =  "Availability " + id + ":\n" +
                "\tRide: \n------------" + ride.toString() + "------------\n" +
                "\tUser: \n------------" + user.toString() + "------------\n" +
                "\tStop: \n------------" + stop.toString() + "------------\n" +
                "\tConfirmed: \n------------" + confirmed + "------------\n" +
                "\tViewed: \n------------" + viewed + "------------\n";
        return result;
    }
}

