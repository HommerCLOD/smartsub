package ua.smartsub.smartsub.services.implentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.smartsub.smartsub.model.DTO.DeviceInfo;
import ua.smartsub.smartsub.dao.UserDeviceDao;
import ua.smartsub.smartsub.model.entity.RefreshToken;
import ua.smartsub.smartsub.model.entity.UserDevice;
import ua.smartsub.smartsub.exception.TokenRefreshException;
import ua.smartsub.smartsub.services.IUserDeviceService;

import java.util.Optional;

@Service
@Slf4j
public class UserDeviceService implements IUserDeviceService {

    @Autowired
    private  UserDeviceDao userDeviceDao;


         /* Find the user device info by user id
     */
    public Optional<UserDevice> findByUserId(Long userId) {
        return userDeviceDao.findByUserId(userId);
    }

         /* Find the user device info by refresh token
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceDao.findByRefreshToken(refreshToken);
    }

    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setNotificationToken(deviceInfo.getNotificationToken());
        userDevice.setIsRefreshActive(true);
        return userDevice;
    }
    public void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "No device found for the matching token. Please login again"));

        if (!userDevice.getIsRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh blocked for the device. Please login through a different device");
        }
    }
}
