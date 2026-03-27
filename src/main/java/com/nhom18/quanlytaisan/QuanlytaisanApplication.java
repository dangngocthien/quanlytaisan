package com.nhom18.quanlytaisan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuanlytaisanApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanlytaisanApplication.class, args);
	}
}
