package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DemoApplication {
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder  application){
//        return application.sources(DemoApplication.class);
//    }
	public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
	}

}
