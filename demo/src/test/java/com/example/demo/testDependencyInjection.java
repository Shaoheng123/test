package com.example.demo;

import com.example.demo.Components.Biker;
import com.example.demo.Components.Driver;
import com.example.demo.Entity.*;
import com.example.demo.config.EmployeeConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class testDependencyInjection {
    @Autowired
    Driver driver;

    @Autowired
    Biker biker;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testDriver() {
        assertTrue(driver.vehicle instanceof Truck);
    }
    @Test
    public void testBiker() {
        assertTrue(biker.vehicle instanceof Bike);

    }

    @Test
    public void testEmployee() {
        Employee employee = applicationContext.getBean(Employee.class);
        assertEquals(employee.getName(), "John");
    }

    @Test
    public void testInjectionPrototype() {
        //Singleton: Driver,
        // Prototype: Biker
        Driver firstSingleton = applicationContext.getBean(Driver.class);
        Biker firstSingletonPrototypeBiker = firstSingleton.getPrototypeBiker();
        Driver secondSingleton = applicationContext.getBean(Driver.class);
        Biker secondSingletonPrototypeBiker = secondSingleton.getPrototypeBiker();
        assertEquals(firstSingletonPrototypeBiker,secondSingletonPrototypeBiker);


    }
}
