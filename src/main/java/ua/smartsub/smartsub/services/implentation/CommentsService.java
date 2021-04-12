package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.CommentsDao;
import ua.smartsub.smartsub.dao.SubscribeDao;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.model.entity.Comments;
import ua.smartsub.smartsub.model.entity.Subscribe;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.ICommentsService;

import java.util.List;
import java.util.Set;

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
        User user = userDao.findById(user_id).orElseThrow(()->new UniqueUserException("Такий Юзер не існує"));
        Subscribe subscribe = subscribeDao.findById(subscribe_id).orElseThrow(()->new UniqueUserException("Такий Пост не існує"));
        comment.setUser(user);
        comment.setSubscribe(subscribe);
        return commentsDao.save(comment);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentsDao.deleteById(id);
    }

    @Override
    public Comments editComment(Long id, Comments comment) {
        Comments com = commentsDao.findById(id).orElseThrow(()-> new RuntimeException("Not found comment"));
        com.setText(comment.getText());
        return com;
    }

    @Override
    public List<Comments> allComments() {
        return commentsDao.findAll();
    }
}
