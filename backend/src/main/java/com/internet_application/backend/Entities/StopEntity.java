package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="stop")
public class StopEntity {
    @Id
    @JsonProperty("id_stop")
    @Column(name = "id_stop")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("stop_name")
    @Column(name = "stop_name")
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    private String gps;

    @OneToMany(mappedBy = "stop")
    @JsonManagedReference(value = "stop")
    private Set<LineStopEntity> lineStops;

    @OneToMany(mappedBy = "joinStop")
    @JsonIgnore
    private Set<ReservationEntity> joinReservation;

    @OneToMany(mappedBy = "leaveStop")
    @JsonIgnore
    private Set<ReservationEntity> leaveReservation;

    public Set<LineStopEntity> getLineStops() {
        return lineStops;
    }

    public void setLineStops(Set<LineStopEntity> lineStops) {
        this.lineStops = lineStops;
    }

    public Set<ReservationEntity> getJoinReservation() {
        return joinReservation;
    }

    public void setJoinReservation(Set<ReservationEntity> joinReservation) {
        this.joinReservation = joinReservation;
    }

    public Set<ReservationEntity> getLeaveReservation() {
        return leaveReservation;
    }

    public void setLeaveReservation(Set<ReservationEntity> leaveReservation) {
        this.leaveReservation = leaveReservation;
    }

    @Override
    public String toString() {
        String result =  "Stop " + id + ":\n" +
                "\tName: " + name + "\n" +
                "\tGPS: " + gps + "\n";;
        return result;
    }
}
