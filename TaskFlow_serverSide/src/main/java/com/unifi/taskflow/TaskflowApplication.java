package com.unifi.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class TaskflowApplication {

	public static void main(String[] args) {
		Dotenv.load(); // Carica le variabili d'ambiente dal file .env

		SpringApplication.run(TaskflowApplication.class, args);
	}

}
