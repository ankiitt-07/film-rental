package com.filmrental;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FilmRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmRentalApplication.class, args);
        log.info("Project Started BackEnd ");
    }

}
