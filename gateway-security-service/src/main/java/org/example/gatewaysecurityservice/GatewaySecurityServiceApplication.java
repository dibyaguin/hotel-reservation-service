package org.example.gatewaysecurityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class GatewaySecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewaySecurityServiceApplication.class, args);
    }

}