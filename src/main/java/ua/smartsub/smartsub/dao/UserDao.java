package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.model.entity.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Long> {

    @Query("select u from User u where u.username=:username ")
    Optional<User> findByName(String username);

    Boolean existsByUsername(String username);

    @Query("select u from User u where u.id=:id ")
    Optional<User> findById(int id);

    @Query("select u from User u where u.email=:email")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
