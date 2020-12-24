package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.User;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserDao userDao;

    @Override
    public User registrationUser(User user) {
        if (userDao.findByName(user.getUsername()) !=null ){
            throw new RuntimeException("Такий юзер вже існує");
        }
        return userDao.save(user);
    }

    @Override
    public void activateAccount(String token) {
    }


}
