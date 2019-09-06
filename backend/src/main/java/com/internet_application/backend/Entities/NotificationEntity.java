package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.internet_application.backend.Serializers.NotificationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
@JsonSerialize(using = NotificationSerializer.class)
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("notificationId")
    @Column(name="id_notification")
    @Getter
    @Setter
    private Long id;

    //@JsonIgnore
    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private UserEntity user;

    @JsonProperty("message")
    @Column(name="message")
    @Getter
    @Setter
    private String message;

    @JsonProperty("link")
    @Column(name="link")
    @Getter
    @Setter
    private String link;

    @JsonProperty("viewed")
    @Column
    @Getter
    @Setter
    private Boolean viewed;

    @Column
    @Type(type = "date")
    @Getter
    @Setter
    private Date date;

    public void setUser(UserEntity user) { this.user = user; }
    public UserEntity getUser() { return this.user; }
}
