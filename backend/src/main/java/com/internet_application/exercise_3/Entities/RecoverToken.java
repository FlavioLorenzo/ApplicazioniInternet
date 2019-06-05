package com.internet_application.exercise_3.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "recover_token")
public class RecoverToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    @Getter
    @Setter
    private long id;

    @Column(name = "recover_token")
    @Getter
    @Setter
    private String recoverToken;

    @Column(name = "creation_date")
    @Getter
    @Setter
    private Date creationDate;

    @Column(name = "is_valid")
    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
    @Getter
    @Setter
    private UserEntity user;

    /*
     * Don't know why Lombok is not working with this var and too tired to think straight
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void createToken() {
        recoverToken = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        String result =  "Reservation " + id + ":\n" +
                "\tRide: " + recoverToken + "\n" +
                "\tRide: " + creationDate + "\n" +
                "\tUser: \n------------" + user.toString() + "------------\n";
        return result;
    }
}