package com.loser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootConfiguration
@SpringBootApplication
public class MogoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MogoApplication.class, args);
    }

}
