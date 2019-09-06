package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Enums.ShiftStatus;
import com.internet_application.backend.Serializers.AvailabilitySerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "availability")
@JsonSerialize(using = AvailabilitySerializer.class)
public class AvailabilityEntity {
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

    @JsonProperty("shiftStatus")
    @Column(name="shift_status")
    @Getter
    @Setter
    private ShiftStatus shiftStatus;

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

    public ShiftStatus getShiftStatus() { return this.shiftStatus;}

    @Override
    public String toString() {
        String result =  "AvailabilityEntity " + id + ":\n" +
                "\tRide: \n------------" + ride.toString() + "------------\n" +
                "\tUser: \n------------" + user.toString() + "------------\n" +
                "\tStop: \n------------" + stop.toString() + "------------\n" +
                "\tShift Status: \n------------" + shiftStatus.getDescription() + "------------\n";
        return result;
    }
}

