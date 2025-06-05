package it.uniroma3.siw.siw_jpa_es1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "it.uniroma3.siw")
public class SiwJpaEs1Application {

	public static void main(String[] args) {
		SpringApplication.run(SiwJpaEs1Application.class, args);
		System.out.println("🚀 Spring Boot è partito!");
	}

}
