package com.futurevet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.Clock;

@SpringBootApplication
public class FutureVetApplication {
    public static void main(String[] args) {
        SpringApplication.run(FutureVetApplication.class, args);
    }

    @Bean
    Clock clock() {
        return Clock.system(java.time.ZoneId.of("America/Sao_Paulo"));
    }
}
