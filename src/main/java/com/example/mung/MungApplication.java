package com.example.mung;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.example.mung.repository"})
@EntityScan(basePackages = {"com.example.mung.entity"})
public class MungApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungApplication.class, args);
	}

}
