package ua.smartsub.smartsub.services;

import ua.smartsub.smartsub.model.DTO.DeviceInfo;
import ua.smartsub.smartsub.model.entity.RefreshToken;
import ua.smartsub.smartsub.model.entity.UserDevice;

import java.util.Optional;

public interface IUserDeviceService {

    Optional<UserDevice> findByUserId(Long userId);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    UserDevice createUserDevice(DeviceInfo deviceInfo);

    void verifyRefreshAvailability(RefreshToken refreshToken);
}
