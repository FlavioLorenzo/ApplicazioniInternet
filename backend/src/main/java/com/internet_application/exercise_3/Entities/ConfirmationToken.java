package com.internet_application.exercise_3.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_token")
    @Getter
    @Setter
    private long idToken;

    @Column(name="confirmation_token")
    @Getter
    @Setter
    private String confirmationToken;

    @Column(name="creation_date")
    @Getter
    @Setter
    private Date creationDate;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id_user")
    @Getter
    @Setter
    private UserEntity user;

    public void createToken() {
        confirmationToken = UUID.randomUUID().toString();
    }
}