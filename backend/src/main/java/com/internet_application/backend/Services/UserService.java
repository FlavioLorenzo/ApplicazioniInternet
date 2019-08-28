package com.internet_application.backend.Services;

import com.internet_application.backend.Entities.UserEntity;

import java.util.List;

public interface UserService {
    void save(UserEntity user);
    void saveAll(List<UserEntity> users);

    UserEntity findByEmail(String email);
    List<UserEntity> getAllUsers();
    String login(String email, String password);
    void register(String email, String firstName, String lastName, String roleName);
    UserEntity getAccountConfirmationInfo(String token);
    void completeAccount(String token, String password, String confirmPassword, String phone);
    void recoverAccount(String email);
    void restorePassword(String password, String confirmPassword, String randomUUID);
    void modifyRole(Long userId, String line, String role);
    boolean checkEmail(String email);
}
