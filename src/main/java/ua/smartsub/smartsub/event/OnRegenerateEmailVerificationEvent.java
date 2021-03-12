package ua.smartsub.smartsub.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;
import ua.smartsub.smartsub.model.entity.EmailVerificationToken;
import ua.smartsub.smartsub.model.entity.User;

public class OnRegenerateEmailVerificationEvent extends ApplicationEvent {

    private transient UriComponentsBuilder redirectUrl;
    @Autowired
    private User user;
    private transient EmailVerificationToken token;

    public OnRegenerateEmailVerificationEvent(User user, UriComponentsBuilder redirectUrl, EmailVerificationToken token) {
        super(user);
        this.user = user;
        this.redirectUrl = redirectUrl;
        this.token = token;
    }
    public UriComponentsBuilder getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EmailVerificationToken getToken() {
        return token;
    }

    public void setToken(EmailVerificationToken token) {
        this.token = token;
    }

}
