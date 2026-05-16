package me.maxim.cvcontactme.service;

import me.maxim.cvcontactme.entity.Feedback;
import me.maxim.cvcontactme.repository.FeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private static final String VIA_TELEGRAM = "telegram";
    private static final String VIA_EMAIL = "email";
    private static final String VIA_FAILED = "failed";

    private final TelegramNotifier telegramNotifier;
    private final EmailNotifier emailNotifier;
    private final FeedbackRepository feedbackRepository;

    public ContactService(TelegramNotifier telegramNotifier,
                          EmailNotifier emailNotifier,
                          FeedbackRepository feedbackRepository) {
        this.telegramNotifier = telegramNotifier;
        this.emailNotifier = emailNotifier;
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Sends notification via Telegram; falls back to Email on failure.
     * Always persists the feedback record regardless of delivery outcome.
     *
     * @throws RuntimeException if all channels failed
     */
    public void send(String name, String email, String message) {
        String sentVia;

        try {
            telegramNotifier.send(name, email, message);
            sentVia = VIA_TELEGRAM;
        } catch (TelegramApiException | RuntimeException tgEx) {
            log.warn("Telegram notification failed, falling back to email. Reason: {}", tgEx.getMessage());
            try {
                emailNotifier.send(name, email, message);
                sentVia = VIA_EMAIL;
            } catch (Exception mailEx) {
                log.error("Email notification also failed. Reason: {}", mailEx.getMessage());
                sentVia = VIA_FAILED;
            }
        }

        Feedback feedback = new Feedback(name, email, message);
        feedback.setSentVia(sentVia);
        feedbackRepository.save(feedback);
        log.info("Feedback saved. sentVia={}, email={}", sentVia, email);

        if (VIA_FAILED.equals(sentVia)) {
            throw new RuntimeException("All notification channels failed");
        }
    }
}
