package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ride")
public class RideEntity {
    @Id
    @JsonProperty("id_ride")
    @Column(name="id_ride")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("date")
    @Column
    @Type(type = "date")
    @Getter
    @Setter
    private Date date;

    @JsonProperty("direction")
    @Column
    @Getter
    @Setter
    private Boolean direction; // 0 - Forth / 1 - Back

    @JsonProperty("id_line")
    @ManyToOne
    @JoinColumn(name = "id_line")
    private BusLineEntity line;

    @OneToMany(mappedBy = "ride")
    @JsonManagedReference("ride_reservation")
    private Set<ReservationEntity> reservations;

    public BusLineEntity getLine() {
        return line;
    }

    public void setLine(BusLineEntity line) {
        this.line = line;
    }

    public Set<ReservationEntity> getReservation() {
        return reservations;
    }

    public void setReservation(Set<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        String result =  "Ride " + id + ":\n" +
                "\tDate: " + date + "\n" +
                "\tDirection: " + direction + "\n" +
                "\tline: \n" + line.toString();
        return result;
    }
}