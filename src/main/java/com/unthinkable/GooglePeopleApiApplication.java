package com.unthinkable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GooglePeopleApiApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(GooglePeopleApiApplication.class, args);
    }
}
