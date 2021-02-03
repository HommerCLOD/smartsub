package ua.smartsub.smartsub.event.listener;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ua.smartsub.smartsub.entity.PasswordResetToken;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.event.OnGenerateResetLinkEvent;
import ua.smartsub.smartsub.exception.MailSendException;
import ua.smartsub.smartsub.services.implentation.MailService;

import javax.mail.MessagingException;
import java.io.IOException;


@Slf4j
@Component
public class OnGenerateResetLinkEventListener implements ApplicationListener<OnGenerateResetLinkEvent> {

    @Autowired
    private MailService mailService;


    /**
     * As soon as a forgot password link is clicked and a valid email id is entered,
     * Reset password link will be sent to respective mail via this event
     */
    @Override
    @Async
    public void onApplicationEvent(OnGenerateResetLinkEvent onGenerateResetLinkMailEvent) {
        sendResetLink(onGenerateResetLinkMailEvent);
    }

    /**
     * Sends Reset Link to the mail address with a password reset link token
     */
    private void sendResetLink(OnGenerateResetLinkEvent event) {
        PasswordResetToken passwordResetToken = event.getPasswordResetToken();
        User user = passwordResetToken.getUser();
        String recipientAddress = user.getEmail();
        String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", passwordResetToken.getToken())
                .toUriString();
        try {
            mailService.sendResetLink(emailConfirmationUrl, recipientAddress);
        } catch (IOException | TemplateException | MessagingException e) {
            log.error(e.toString());
            throw new MailSendException(recipientAddress, "Email Verification");
        }
    }


}
