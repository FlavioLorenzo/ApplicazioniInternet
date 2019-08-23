package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.ChildEntity;
import com.internet_application.backend.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Long> {
}
