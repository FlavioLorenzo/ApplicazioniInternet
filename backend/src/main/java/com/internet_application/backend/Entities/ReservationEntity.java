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

    @JsonProperty("join_stop")
    @ManyToOne
    @JoinColumn(name = "id_join_stop", referencedColumnName = "id_stop")
    private StopEntity joinStop;

    @JsonProperty("leave_stop")
    @ManyToOne
    @JoinColumn(name = "id_leave_stop", referencedColumnName = "id_stop")
    private StopEntity leaveStop;

    public StopEntity getJoinStop() {
        return joinStop;
    }

    public void setJoinStop(StopEntity joinStop) {
        this.joinStop = joinStop;
    }

    public StopEntity getLeaveStop() {
        return leaveStop;
    }

    public void setLeaveStop(StopEntity leaveStop) {
        this.leaveStop = leaveStop;
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
        String result =  "Reservation " + id + ":\n" +
                "\tRide: \n------------" + ride.toString() + "------------\n" +
                "\tUser: \n------------" + user.toString() + "------------\n" +
                "\tJoin Stop: \n------------" + joinStop.toString() + "------------\n" +
                "\tLeave Stop: \n------------" + leaveStop.toString() + "------------\n";
        return result;
    }
}

