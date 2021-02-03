package ua.smartsub.smartsub.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;
import ua.smartsub.smartsub.entity.User;

@Data
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {
    private transient UriComponentsBuilder redirectUrl;
    private User user;

    public OnUserRegistrationCompleteEvent(User user, UriComponentsBuilder redirectUrl) {
        super(user);
        this.user = user;
        this.redirectUrl = redirectUrl;
    }

}
