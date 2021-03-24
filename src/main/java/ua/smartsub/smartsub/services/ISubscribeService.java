package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.model.entity.Subscribe;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ISubscribeService {
    Optional<Subscribe> saveSubscribe (Subscribe subscribe);
    Optional<Subscribe> updateSubscribe (Subscribe subscribe, long id);
    Optional<Subscribe> findSubscribeById(long id);
    List<Subscribe> findSubscribeByAll(String keyword);
    void removeById(long id);
}
