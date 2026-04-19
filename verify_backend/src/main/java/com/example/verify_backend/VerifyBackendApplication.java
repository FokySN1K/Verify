package com.example.verify_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VerifyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VerifyBackendApplication.class, args);
    }

}
