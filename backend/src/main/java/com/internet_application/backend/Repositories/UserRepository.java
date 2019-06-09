package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.email = ?1")
    UserEntity findByEmail(String email);
}
