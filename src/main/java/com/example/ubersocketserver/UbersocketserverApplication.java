package com.example.ubersocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.example.UberProjectEntityService.models")
public class UbersocketserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbersocketserverApplication.class, args);
	}

}
