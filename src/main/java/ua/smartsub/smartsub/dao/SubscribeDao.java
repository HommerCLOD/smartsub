package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.entity.Subscribe;

public interface SubscribeDao extends JpaRepository<Subscribe,Long> {

    @Query("select s from Subscribe s where s.id=:id ")
    Subscribe findById(int id);

}
