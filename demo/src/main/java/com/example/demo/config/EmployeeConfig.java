package com.example.demo.config;

import com.example.demo.Entity.Employee;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class EmployeeConfig {
    @Primary
    @Bean
    public Employee JohnEmployee() {
        return new Employee("John");
    }

    @Bean
    public Employee TonyEmployee() {
        return new Employee("Tony");
    }
}
