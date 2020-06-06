package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableScheduling  //Springboot支持定时任务
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run (DemoApplication.class, args);
    }
    @RequestMapping
    public String index(){
        return "1";
    }
}
