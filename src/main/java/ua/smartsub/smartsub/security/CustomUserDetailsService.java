package ua.smartsub.smartsub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.services.implentation.AuthService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByLogin(username);
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
