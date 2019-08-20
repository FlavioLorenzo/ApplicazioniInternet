package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/*
    @MappedBy
    The mappedBy property is what we use to tell Hibernate which variable we are using to represent the parent
    class in our child class.
 */


@Entity
@Table(name="busline")
public class BusLineEntity {

    @Id
    @JsonProperty("id_line")
    @Column(name="id_line")
    @Getter
    @Setter
    private Long id;

    @NotBlank
    @JsonProperty("name")
    @Column(name="name")
    @Getter
    @Setter
    private String name;

    @JsonProperty("id_admin")
    @ManyToOne
    @JoinColumn(name = "id_admin", referencedColumnName = "id_user")
    private UserEntity admin;

    @OneToMany(mappedBy = "line")
    @JsonIgnore
    private Set<LineStopEntity> outwordStops;

    @OneToMany(mappedBy = "line")
    @JsonIgnore
    private Set<LineStopEntity> returnStops;

    @OneToMany(mappedBy = "line")
    @JsonIgnore
    private Set<RideEntity> rides;

    public Set<LineStopEntity> getOutwordStops() {
        return outwordStops;
    }

    public void setOutwordStops(Set<LineStopEntity> outwordStops) {
        this.outwordStops = outwordStops;
    }

    public Set<LineStopEntity> getReturnStops() {
        return returnStops;
    }

    public void setReturnStops(Set<LineStopEntity> returnStops) {
        this.returnStops = returnStops;
    }

    public Set<RideEntity> getRides() {
        return rides;
    }

    public void setRides(Set<RideEntity> rides) {
        this.rides = rides;
    }

    public UserEntity getAdmin() {
        return admin;
    }

    public void setAdmin(UserEntity admin) { this.admin = admin; }

    @Override
    public String toString() {
        String result =  "Busline " + id + ":\n" +
                "\tName: " + name + "\n";
        return result;
    }
}
