package me.maxim.cvcontactme.service;

import me.maxim.cvcontactme.bot.CvBot;
import me.maxim.cvcontactme.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramNotifier {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotifier.class);

    private final CvBot cvBot;
    private final AppProperties appProperties;

    public TelegramNotifier(CvBot cvBot, AppProperties appProperties) {
        this.cvBot = cvBot;
        this.appProperties = appProperties;
    }

    public void send(String name, String email, String message) throws TelegramApiException {
        String chatId = appProperties.getTelegram().getChatId();
        if (chatId == null || chatId.isEmpty()) {
            throw new TelegramApiException("TELEGRAM_CHAT_ID is not configured");
        }

        String text = String.format(
                "📩 *Новое сообщение с CV*%n%n" +
                "*Имя:* %s%n" +
                "*Email:* %s%n%n" +
                "*Сообщение:*%n%s",
                escape(name), escape(email), escape(message)
        );

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode("MarkdownV2");

        cvBot.execute(sendMessage);
        log.info("Telegram notification sent for email={}", email);
    }

    private String escape(String text) {
        if (text == null) return "";
        return text
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }
}
