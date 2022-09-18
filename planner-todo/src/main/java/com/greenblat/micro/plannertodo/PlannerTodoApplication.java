package com.greenblat.micro.plannertodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.greenblat.micro"})
@EnableJpaRepositories(basePackages = {"com.greenblat.micro.plannertodo"})
@EnableFeignClients
public class PlannerTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerTodoApplication.class, args);
    }

}
