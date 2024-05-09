package com.example.VolunteerWebApp;

import com.example.VolunteerWebApp.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@Import(SwaggerConfig.class)
public class VolunteerWebAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(VolunteerWebAppApplication.class, args);
	}

}
