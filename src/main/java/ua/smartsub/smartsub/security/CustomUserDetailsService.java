package ua.smartsub.smartsub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.services.IAuthService;
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IAuthService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username).orElseThrow(()->  new UsernameNotFoundException(username));
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
