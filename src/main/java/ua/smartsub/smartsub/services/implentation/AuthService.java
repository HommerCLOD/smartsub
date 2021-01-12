package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.IAuthService;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserDao userDao;

    @Override
    public User registrationUser(User user) {
        if (userDao.findByName(user.getUsername()) !=null){
            throw new UniqueUserException("Юзер з таким логіном вже існує");
        } else if (userDao.findByEmail(user.getEmail()) !=null){
            throw new UniqueUserException("Юзер з таким е-маіл вже існує");
        }
        return userDao.save(user);
    }

    @Override
    public void activateAccount(String token) {
    }


}
