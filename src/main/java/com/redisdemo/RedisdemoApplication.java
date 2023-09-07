package com.redisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@SpringBootApplication
public class RedisdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisdemoApplication.class, args);
	}

}
