package com.emi.biddingengine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BiddingengineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiddingengineApplication.class, args);
	}

		@Bean
    public CommandLineRunner printEnv() {
        return args -> {
            System.out.println("NEON_DATABASE_URL = " + System.getenv("NEON_DATABASE_URL"));
        };
    }
}
