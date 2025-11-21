package com.example.demo.config;

import com.example.demo.Components.Biker;
import com.example.demo.Components.Driver;
import com.example.demo.Entity.Car;
import com.example.demo.Entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:value.properties")
public class VehicleConfig {
    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Biker getBiker() {
        return new Biker();
    }

    @Bean
    public Driver getDriver() {
        return new Driver();
    }
}
