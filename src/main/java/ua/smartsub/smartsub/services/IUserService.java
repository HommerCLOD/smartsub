package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.annotation.CurrentUser;
import ua.smartsub.smartsub.model.DTO.LogOutDTO;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.security.CustomUserDetails;

import java.util.Optional;

public interface IUserService {
    Boolean existsByEmail(String email);

    Optional<User> findByName(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutDTO logOutDTO);

    User save(User user);
}
