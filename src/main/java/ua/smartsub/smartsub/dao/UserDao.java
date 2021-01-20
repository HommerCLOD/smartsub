package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.entity.User;

public interface UserDao extends JpaRepository<User,Integer> {

    @Query("select u from User u where u.username=:username ")
    User findByName(String username);

    @Query("select u from User u where u.id=:id ")
    User findById(int id);

    @Query("select u from User u where u.email=:email")
    User findByEmail(String email);
}
