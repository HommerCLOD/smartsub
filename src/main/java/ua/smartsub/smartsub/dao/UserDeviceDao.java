package ua.smartsub.smartsub.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.smartsub.smartsub.model.entity.RefreshToken;
import ua.smartsub.smartsub.model.entity.UserDevice;

import java.util.Optional;

public interface UserDeviceDao extends JpaRepository<UserDevice, Long> {

    @Override
    Optional<UserDevice> findById(Long id);

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserId(Long userId);
}
