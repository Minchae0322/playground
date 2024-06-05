package com.example.playgroundmanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlaygroundManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundManageApplication.class, args);
    }

}
