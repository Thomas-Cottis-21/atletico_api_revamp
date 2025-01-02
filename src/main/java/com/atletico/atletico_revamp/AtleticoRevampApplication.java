package com.atletico.atletico_revamp;

import com.atletico.atletico_revamp.config.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfigurationProperties.class)
public class AtleticoRevampApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtleticoRevampApplication.class, args);
	}

}
