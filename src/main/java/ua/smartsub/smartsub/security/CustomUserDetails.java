package ua.smartsub.smartsub.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.smartsub.smartsub.entity.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private boolean status_active_account;

    public static CustomUserDetails fromUserToCustomUserDetails(User user) {
        CustomUserDetails c = new CustomUserDetails();
        c.login = user.getUsername();
        c.password = user.getPassword();
        c.status_active_account = user.getStatus_active_account();
        c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


// Потрібно прикрути Емаіл перевірку чи активований користувач
//    @Override
//    public boolean isAccountNonLocked() {
//        return status_active_account;
//    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
