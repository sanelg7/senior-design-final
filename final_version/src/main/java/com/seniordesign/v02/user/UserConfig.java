package com.seniordesign.v02.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
// Uncomment if you want to see mock users when initialized.
@Configuration
public class UserConfig {
/*
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository){
        return args -> {
            User ege = new User(
                    "Ege",
                    "password",
                    "sanelege@gmail.com"
        );
            User bunyo = new User(
                    "Bünyamin",
                    "cussword",
                    "cunyamin@cunmail.com"
            );
            repository.saveAll(
                    List.of(ege,bunyo)
            );

        };
    }*/
}
