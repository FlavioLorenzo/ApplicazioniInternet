package com.internet_application.exercise_3.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_user")
    @Column(name="id_user")
    @Getter
    @Setter
    private Long id;

    @JsonProperty("first_name")
    @Column(name="first_name")
    @Getter
    @Setter
    private String firstName;

    @JsonProperty("last_name")
    @Column(name="last_name")
    @Getter
    @Setter
    private String lastName;

    @JsonProperty("phone")
    @Column
    @Getter
    @Setter
    private String phone;

    @Email
    @JsonProperty("email")
    @Column
    @Getter
    @Setter
    private String email;

    @Column(name="password")
    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToOne
    @JoinColumn(name = "id_role", referencedColumnName = "id_role")
    private RoleEntity role;

    @Column
    @Getter
    @Setter
    private Boolean enabled;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ReservationEntity> reservations;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RecoverToken> recoverTokens;

    @JsonIgnore
    public String getPassword() { return password; }

    @JsonProperty("password")
    public void setPassword(String password) { this.password = password; }

    @JsonIgnore
    public String getPasswordConfirm() { return passwordConfirm; }

    @JsonProperty("password_confirm")
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    @JsonProperty("role")
    public RoleEntity getRole() { return role; }

    @JsonIgnore
    public void setRole(RoleEntity role) { this.role = role; }

    public Set<ReservationEntity> getReservation() {
        return reservations;
    }

    public void setReservation(Set<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public Set<RecoverToken> getRecoverTokens() {
        return recoverTokens;
    }

    public void setRecoverTokens(Set<RecoverToken> recoverTokens) {
        this.recoverTokens = recoverTokens;
    }

    @Override
    public String toString() {
        String result =  "User " + id + ":\n" +
                "\tFirst Name: " + firstName + "\n" +
                "\tLast Name: " + lastName + "\n" +
                "\tPhone: " + phone + "\n" +
                "\tEmail: " + email + "\n" +
                "\tIs enabled: " + enabled + "\n" +
                "\tRole: \n------------" + role.toString() + "------------";
        return result;
    }
}