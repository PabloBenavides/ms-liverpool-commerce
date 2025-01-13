package com.liverpool.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = "com.liverpool.api")
@EntityScan(basePackages = "com.liverpool.api.*")
@EnableMongoRepositories(basePackages = "com.liverpool.api")
@PropertySource({"classpath:application.properties"})
@SpringBootApplication
public class MsLiverpoolCommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLiverpoolCommerceApplication.class, args);
	}

}
