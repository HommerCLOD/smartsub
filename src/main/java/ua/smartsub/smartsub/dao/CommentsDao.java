package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.smartsub.smartsub.entity.Comments;

public interface CommentsDao extends  JpaRepository<Comments,Long> {
}
