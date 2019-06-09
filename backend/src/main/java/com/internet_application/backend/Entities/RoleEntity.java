package com.internet_application.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class RoleEntity {
    @Id
    @JsonProperty("id_role")
    @Column(name="id_role")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @JsonProperty("name")
    @Column
    @Getter
    @Setter
    public String name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserEntity> users;

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void getUsers(Set<UserEntity> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        String result =  "Role " + id + ":\n" +
                "\tName: " + name;
        return result;
    }
}
