package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Point;
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

    @JsonProperty("name")
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    @JsonIgnore
    private Point gps;

    @OneToMany(mappedBy = "stop")
    @JsonManagedReference(value = "stop")
    private Set<LineStopEntity> lineStops;

    @OneToMany(mappedBy = "stop")
    @JsonIgnore
    private Set<ReservationEntity> reservations;

    @OneToMany(mappedBy = "stop")
    @JsonIgnore
    private Set<Availability> availabilities;

    public Set<LineStopEntity> getLineStops() {
        return lineStops;
    }

    public void setLineStops(Set<LineStopEntity> lineStops) {
        this.lineStops = lineStops;
    }

    public Set<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    @Override
    public String toString() {
        String result =  "Stop " + id + ":\n" +
                "\tName: " + name + "\n" +
                "\tGPS: " + gps + "\n";;
        return result;
    }
}
