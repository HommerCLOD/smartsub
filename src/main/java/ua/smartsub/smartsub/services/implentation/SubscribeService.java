package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.dao.SubscribeDao;
import ua.smartsub.smartsub.model.entity.Subscribe;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.ISubscribeService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscribeService implements ISubscribeService {
    @Autowired
    private SubscribeDao subscribeDao;

    @Override
    public Optional<Subscribe> saveSubscribe(Subscribe subscribe) {
        if (botnameAlreadyExists(subscribe.getName())) {
            log.error("Bot_name already exists: " + subscribe.getName());
            throw new UniqueUserException("Bot з такою назвою вже існує");
        } else if (boturlAlreadyExists(subscribe.getUrl())) {
            log.error("URL already exists: " + subscribe.getUrl());
            throw new UniqueUserException("Bot з таким посиланням вже існує");
        }
        log.info("Trying to register new user [" + subscribe.getName() + "]");

        return Optional.of(subscribeDao.save(subscribe));
    }

    public Optional<Subscribe> findSubscribeById(long id) {
        Subscribe subscribe = subscribeDao.findById(id).orElseThrow(()->  new UniqueUserException("Такий Пост не існує"));
        return Optional.of(subscribe);
    }

    public List<Subscribe> findSubscribeByAll(String keyword) {
        List<Subscribe> listSub;
        if (keyword != null){
            listSub = subscribeDao.findAll(keyword);
        }else{
            listSub = subscribeDao.findAll();
        }
        return listSub;
    }

    public Boolean botnameAlreadyExists(String bot_name) {
        return subscribeDao.existsByName(bot_name);
    }

    public Boolean boturlAlreadyExists(String bot_url) {
        return subscribeDao.existsByUrl(bot_url);
    }

    @Override
    public void removeById(long id) {
        if (subscribeDao.existsById(id)) {
            subscribeDao.deleteById(id);
        }
    }

    @Override
    public Optional<Subscribe> updateSubscribe(Subscribe subscribe, long id) {
        if (subscribeDao.existsById(id)) {
            subscribe.setId(id);
           return Optional.of(subscribeDao.save(subscribe));
        }

        return Optional.empty();
    }
}
