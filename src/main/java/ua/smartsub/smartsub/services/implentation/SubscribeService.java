package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.SubscribeDao;
import ua.smartsub.smartsub.entity.Subscribe;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.ISubscribeService;

@Service
public class SubscribeService implements ISubscribeService{
    @Autowired
    private SubscribeDao subscribeDao;
    @Override
    public Subscribe saveSubscribe(Subscribe subscribe) {
        return subscribeDao.save(subscribe);
    }

    public Subscribe findSubscribeById(int id){
        Subscribe subscribe = subscribeDao.findById(id);
        if (subscribe == null){
            throw new UniqueUserException("Такий Пост не існує");
        }
        return subscribe;
    }
}
