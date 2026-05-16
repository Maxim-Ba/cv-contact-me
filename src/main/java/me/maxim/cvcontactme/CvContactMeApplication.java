package me.maxim.cvcontactme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CvContactMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvContactMeApplication.class, args);
    }
}
