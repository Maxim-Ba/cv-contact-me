package me.maxim.cvcontactme.bot;

import me.maxim.cvcontactme.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CvBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(CvBot.class);

    private final AppProperties appProperties;

    public CvBot(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public String getBotUsername() {
        return appProperties.getTelegram().getBotUsername();
    }

    @Override
    public String getBotToken() {
        return appProperties.getTelegram().getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getFrom().getFirstName();

        log.info("Received message from chatId={}: {}", chatId, text);

        if ("/start".equals(text)) {
            sendReply(chatId, "Привет, " + firstName + "! Это бот для уведомлений с CV-сайта. Ваш chat id: " + chatId);
        } else if ("/chatid".equals(text)) {
            sendReply(chatId, "Ваш chat id: " + chatId);
        }
    }

    private void sendReply(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send reply to chatId={}", chatId, e);
        }
    }
}
