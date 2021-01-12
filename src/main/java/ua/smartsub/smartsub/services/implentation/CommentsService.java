package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.CommentsDao;
import ua.smartsub.smartsub.dao.SubscribeDao;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.Comments;
import ua.smartsub.smartsub.entity.Subscribe;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.ICommentsService;

@Service
public class CommentsService implements ICommentsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private SubscribeDao subscribeDao;
    @Autowired
    private CommentsDao commentsDao;
    @Override
    public Comments saveComment(Comments comment, int user_id, int subscribe_id) {
        User user = userDao.findById(user_id);
        Subscribe subscribe = subscribeDao.findById(subscribe_id);
        if (user == null || subscribe == null){
            throw new UniqueUserException("Такий юзер або Пост не існує");
        }
        comment.setUser(user);
        comment.setSubscribe(subscribe);
        return commentsDao.save(comment);
    }
}
