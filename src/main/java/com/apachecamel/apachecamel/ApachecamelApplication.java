package com.apachecamel.apachecamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.apache.camel.component.http.HttpComponent;

@SpringBootApplication
public class ApachecamelApplication {
	@Bean({"http","https"})
	HttpComponent httpComponent() {
		return new HttpComponent();
	}
	public static void main(String[] args) {

		SpringApplication.run(ApachecamelApplication.class, args);
	}
}
