package ua.smartsub.smartsub.services.implentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.smartsub.smartsub.dao.RoleDao;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.Role;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.exception.UniqueUserException;
import ua.smartsub.smartsub.services.IAuthService;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        if (userDao.findByName(user.getUsername()) !=null){
            throw new UniqueUserException("Юзер з таким логіном вже існує");
        } else if (userDao.findByEmail(user.getEmail()) !=null){
            throw new UniqueUserException("Юзер з таким е-маіл вже існує");
        }
        Role userRole = roleDao.findByName("ROLE_USER");
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    public User findByLogin(String login) {
        return userDao.findByName(login);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
    @Override
    public void activateAccount(String token) {
    }


}
