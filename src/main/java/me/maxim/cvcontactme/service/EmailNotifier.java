package me.maxim.cvcontactme.service;

import me.maxim.cvcontactme.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotifier {

    private static final Logger log = LoggerFactory.getLogger(EmailNotifier.class);

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    public EmailNotifier(JavaMailSender mailSender, AppProperties appProperties) {
        this.mailSender = mailSender;
        this.appProperties = appProperties;
    }

    public void send(String name, String email, String message) throws MailException {
        String to = appProperties.getSmtp().getTo();
        String from = appProperties.getSmtp().getFrom();

        if (to == null || to.isEmpty()) {
            throw new MailException("SMTP_TO is not configured") {};
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(to);
        mail.setReplyTo(email);
        mail.setSubject("CV — новое сообщение от " + name);
        mail.setText(
                "Имя: " + name + "\n" +
                "Email: " + email + "\n\n" +
                "Сообщение:\n" + message
        );

        mailSender.send(mail);
        log.info("Email notification sent for email={}", email);
    }
}
