package com.example.WebLearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class WebLearnApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebLearnApplication.class, args);
	}

}
