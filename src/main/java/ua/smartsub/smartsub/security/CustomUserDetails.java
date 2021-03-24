package ua.smartsub.smartsub.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.smartsub.smartsub.model.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class CustomUserDetails extends User implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public CustomUserDetails(User user) {
        super(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getTelegram_user_id(),
                user.getEmailVerified(),
                user.getSubs_tokens(),
                user.getRole());
    }

    public static CustomUserDetails fromUserToCustomUserDetails(User user) {
        CustomUserDetails c = new CustomUserDetails(user);
        c.login = user.getUsername();
        c.password = user.getPassword();
        c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getEmailVerified();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getEmailVerified();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomUserDetails that = (CustomUserDetails) obj;
        return Objects.equals(getId(), that.getId());
    }
}
