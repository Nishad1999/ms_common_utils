package org.mystore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.mystore.*"})
@EntityScan(basePackages = {"org.mystore.*"})
@EnableFeignClients(basePackages = {"org.mystore.feignclients"})
public class MyStoreApplication {
    public static void main(String[] args){
        SpringApplication.run(MyStoreApplication.class, args);
    }


}