package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.entity.Comments;

public interface ICommentsService {
    Comments saveComment (Comments comment, int user_id, int subscribe_id);
}
