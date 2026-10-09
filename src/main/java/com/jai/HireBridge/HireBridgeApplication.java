package com.jai.HireBridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HireBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HireBridgeApplication.class, args);
	}
}