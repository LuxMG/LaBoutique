package com.egg.laboutique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LaboutiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaboutiqueApplication.class, args);
	}

}
