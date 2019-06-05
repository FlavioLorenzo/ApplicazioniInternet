package com.internet_application.exercise_3.Repositories;

import com.internet_application.exercise_3.Entities.RecoverToken;
import org.springframework.data.repository.CrudRepository;

public interface RecoverTokenRepository extends CrudRepository<RecoverToken, String> {
    RecoverToken findByRecoverToken(String recoverToken);
}