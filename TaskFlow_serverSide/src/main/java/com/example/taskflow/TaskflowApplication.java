package com.example.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class TaskflowApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // Carica le variabili d'ambiente dal file .env

        // Accedi alle variabili d'ambiente
        String username = dotenv.get("MAIL_USERNAME");
        String password = dotenv.get("MAIL_PASSWORD");
		
		SpringApplication.run(TaskflowApplication.class, args);
	}

}
