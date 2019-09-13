package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Table(name = "child")
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("childId")
    @Column(name="id_child")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("firstName")
    @Column(name="first_name")
    @Getter
    @Setter
    private String firstName;

    @JsonProperty("lastName")
    @Column(name="last_name")
    @Getter
    @Setter
    private String lastName;

    @Pattern(regexp = "(\\+39)?[0-9]{8,12}")
    @JsonProperty("phone")
    @Column
    @Getter
    @Setter
    private String phone;

    @OneToMany(mappedBy = "child",cascade=CascadeType.REMOVE)
    @JsonIgnore
    private Set<ReservationEntity> reservations;

    @JsonProperty("parent")
    @ManyToOne
    @JoinColumn(name = "id_parent", referencedColumnName = "id_user")
    private UserEntity parent;

    public Set<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public UserEntity getParent() {
        return parent;
    }

    public void setParent(UserEntity parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        String result =  "User " + id + ":\n" +
                "\tFirst Name: " + firstName + "\n" +
                "\tLast Name: " + lastName + "\n" +
                "\tPhone: " + phone + "\n";
        return result;
    }
}