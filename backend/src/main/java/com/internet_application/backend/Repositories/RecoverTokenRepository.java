package com.internet_application.backend.Repositories;

import com.internet_application.backend.Entities.RecoverToken;
import org.springframework.data.repository.CrudRepository;

public interface RecoverTokenRepository extends CrudRepository<RecoverToken, String> {
    RecoverToken findByRecoverToken(String recoverToken);
}