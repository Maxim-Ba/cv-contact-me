package me.maxim.cvcontactme.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Telegram telegram = new Telegram();
    private Smtp smtp = new Smtp();
    private Cors cors = new Cors();
    private Geoip geoip = new Geoip();

    @Getter
    @Setter
    public static class Telegram {
        private String botToken;
        private String botUsername;
        private String chatId;
    }

    @Getter
    @Setter
    public static class Smtp {
        private String to;
        private String from;
    }

    @Getter
    @Setter
    public static class Cors {
        private String allowedOrigins = "http://localhost:4200";
    }

    @Getter
    @Setter
    public static class Geoip {
        private String databasePath = "";
    }
}
