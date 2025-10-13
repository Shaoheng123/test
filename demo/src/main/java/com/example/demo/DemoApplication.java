package com.example.demo;

import com.example.demo.config.YAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder  application){
//        return application.sources(DemoApplication.class);
//    }
    @Autowired
    private YAMLConfig myconfig;

	public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class)
                .web(WebApplicationType.SERVLET)
                .run();
	}

    public void run(String[] args) {
        System.out.println(myconfig.getEnvironment());
        System.out.println(myconfig.getName());
        System.out.println(myconfig.isEnabled());
        System.out.println(myconfig.getServers());

    }


}
