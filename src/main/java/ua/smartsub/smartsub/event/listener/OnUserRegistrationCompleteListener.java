package ua.smartsub.smartsub.event.listener;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ua.smartsub.smartsub.model.entity.User;
import ua.smartsub.smartsub.event.OnUserRegistrationCompleteEvent;
import ua.smartsub.smartsub.exception.MailSendException;
import ua.smartsub.smartsub.services.implentation.EmailVerificationTokenService;
import ua.smartsub.smartsub.services.implentation.MailService;

import javax.mail.MessagingException;
import java.io.IOException;

@Component
@Slf4j
public class OnUserRegistrationCompleteListener   implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MailService mailService;


    @Autowired
    public OnUserRegistrationCompleteListener(EmailVerificationTokenService emailVerificationTokenService, MailService mailService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.mailService = mailService;
    }

    /**
     * As soon as a registration event is complete, invoke the email verification
     * asynchronously in an another thread pool
     */
    @Override
    @Async
    public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
        sendEmailVerification(onUserRegistrationCompleteEvent);
    }

    /**
     * Send email verification to the user and persist the token in the database.
     */
    private void sendEmailVerification(OnUserRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = emailVerificationTokenService.generateNewToken();
        emailVerificationTokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", token).toUriString();

        try {
            mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
        } catch (IOException | TemplateException | MessagingException e) {
            log.error(e.toString());
            throw new MailSendException(recipientAddress, "Email Verification");
        }
    }
}
