package com.example.demo.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevDataSourceConfig implements DatasourceConfig {

    @Override
    public void setup() {
        System.out.println("dataSource for dev");
    }
}
