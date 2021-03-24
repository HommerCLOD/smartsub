package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.smartsub.smartsub.model.entity.Subscribe;

import java.util.List;
import java.util.Optional;

public interface SubscribeDao extends JpaRepository<Subscribe,Long> {

    @Query("select s from Subscribe s where s.id=:id ")
    Optional<Subscribe> findById(int id);

    @Query("SELECT s FROM Subscribe s WHERE CONCAT(s.name, s.description) LIKE %?1%")
    List<Subscribe> findAll(String keyword);

    Boolean existsByName(String name);

    Boolean existsByUrl(String url);

}
