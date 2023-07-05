package com.denger.rba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RbaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbaApplication.class, args);
    }

}
