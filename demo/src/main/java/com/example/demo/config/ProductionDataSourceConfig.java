package com.example.demo.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class ProductionDataSourceConfig implements DatasourceConfig{

    @Override
    public void setup() {
        System.out.println("This is Production");
    }
}
