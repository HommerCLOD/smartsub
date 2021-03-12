package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.model.DTO.LogOutDTO;
import ua.smartsub.smartsub.annotation.CurrentUser;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.model.entity.UserDevice;
import ua.smartsub.smartsub.exception.UserLogoutException;
import ua.smartsub.smartsub.security.CustomUserDetails;
import ua.smartsub.smartsub.services.IUserService;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private  UserDeviceService userDeviceService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public Boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }


    public Optional<User> findByName(String username) {
    return userDao.findByName(username);
    }


    public Optional<User> findByEmail(String email) { return userDao.findByEmail(email); }

    public Boolean existsByUsername(String username) {
        return userDao.existsByUsername(username);
    }

    /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for the given user, throw a log out exception.
     */
    public void logoutUser(@CurrentUser CustomUserDetails currentUser, LogOutDTO logOutDTO) {
        String deviceId = logOutDTO.getDeviceInfo().getDeviceId();
        UserDevice userDevice = userDeviceService.findByUserId(currentUser.getId())
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutDTO.getDeviceInfo().getDeviceId(), "Invalid device Id supplied. No matching device found for the given user "));

        log.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
    }
    public User save(User user){
        return userDao.save(user);
    }
}
