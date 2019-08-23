package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.internet_application.backend.Enums.RideBookingStatus;
import com.internet_application.backend.Serializers.RideSerializer;
import com.internet_application.backend.Utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ride")
@JsonSerialize(using = RideSerializer.class)
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

    /*
     * Specifies whether the Ride has already started or if it is terminated
     */
    @Column(name="ride_booking_status")
    @Getter
    @Setter
    private RideBookingStatus rideBookingStatus;

    @ManyToOne
    @JoinColumn(name = "id_latest_stop")
    private StopEntity latestStop;

    @Column(name="latest_stop_time")
    @Type(type = "time")
    @Getter
    @Setter
    private Date latestStopTime;

    @JsonProperty("id_line")
    @ManyToOne
    @JoinColumn(name = "id_line")
    private BusLineEntity line;

    @OneToMany(mappedBy = "ride")
    @JsonManagedReference("ride_reservation")
    private Set<ReservationEntity> reservations;

    @OneToMany(mappedBy = "ride")
    @JsonManagedReference("ride_availability")
    private Set<Availability> availabilities;

    public BusLineEntity getLine() {
        return line;
    }

    public void setLine(BusLineEntity line) {
        this.line = line;
    }

    public StopEntity getLatestStop() {
        return latestStop;
    }

    public void setLatestStop(StopEntity stop) {
        this.latestStop = stop;
    }

    public Set<ReservationEntity> getReservation() {
        return reservations;
    }

    public void setReservation(Set<ReservationEntity> reservations) {
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
        String result =  "Ride " + id + ":\n" +
                "\tDate: " + date + "\n" +
                "\tDirection: " + direction + "\n" +
                "\tline: \n" + line.toString() + "\n" +
                "\tbookingStatus: \n" + rideBookingStatus.getDescription() + "\n" +
                "\tlatest stop: \n" + latestStop.toString() + "\n" +
                "\tlatest_stop_time: \n" + DateUtils.timeToString(latestStopTime);
        return result;
    }
}