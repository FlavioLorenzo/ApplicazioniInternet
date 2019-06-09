package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.UserEntity;

import java.util.List;

public interface UserService {
    void save(UserEntity user);
    void saveAll(List<UserEntity> users);

    UserEntity findByEmail(String email);
    List<UserEntity> getAllUsers();
    String login(String email, String password);
    void register(String email, String password, String confirmPassword, String firstName, String lastName);
    void confirmAccount(String token);
    void recoverAccount(String email);
    void restorePassword(String password, String confirmPassword, String randomUUID);
    void modifyRole(Long userId, String line, String role);
}
