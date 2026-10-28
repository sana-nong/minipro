package com.sit.govopp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GovOppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovOppApplication.class, args);
    }
}
