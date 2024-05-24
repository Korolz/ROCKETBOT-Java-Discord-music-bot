package me.korolz.rocketbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RocketbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketbotApplication.class, args);
    }

}
