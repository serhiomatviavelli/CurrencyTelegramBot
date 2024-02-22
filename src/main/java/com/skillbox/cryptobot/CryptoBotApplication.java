package com.skillbox.cryptobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptoBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoBotApplication.class, args);
    }

}
