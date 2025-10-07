package com.example.demo.Entity;

import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.ApplicationScope;

public class Scope {
    Transmission transmission;
    Long carId;
    Engine engine;
    @Bean
    @ApplicationScope
    public Scope applicationScopedBean() {
        return new Car(engine,transmission, carId);
    }


}
