package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.model.entity.Comments;

import java.util.List;

public interface ICommentsService {
    Comments saveComment (Comments comment, int user_id, int subscribe_id);
    void deleteCommentById (Long id);
    Comments editComment (Long id, Comments comment);
    List<Comments> allComments();


}
