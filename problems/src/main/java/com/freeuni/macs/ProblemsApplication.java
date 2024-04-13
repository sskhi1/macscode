package com.freeuni.macs;

import com.freeuni.macs.service.ProblemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ProblemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemsApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeData(ProblemService problemService) {
        return args -> {

        };
    }

}
