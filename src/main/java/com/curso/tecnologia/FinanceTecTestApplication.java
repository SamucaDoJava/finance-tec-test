package com.curso.tecnologia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FinanceTecTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceTecTestApplication.class, args);
	}

}
