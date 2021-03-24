package ua.smartsub.smartsub.services;

import freemarker.template.TemplateException;
import ua.smartsub.smartsub.model.DTO.Mail;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IMailService {
    void sendEmailVerification(String emailVerificationUrl, String to)
            throws IOException, TemplateException, MessagingException;

    void sendResetLink(String resetPasswordLink, String to)
            throws IOException, TemplateException, MessagingException;

    void sendAccountChangeEmail(String action, String actionStatus, String to)
            throws IOException, TemplateException, MessagingException;

    void send(Mail mail) throws MessagingException;
}
