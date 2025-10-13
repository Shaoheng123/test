package com.example.demo;

import com.example.demo.Entity.Car;
import com.example.demo.config.AccountConfig;
import com.example.demo.config.DevDataSourceConfig;
import com.example.demo.config.ProductionDataSourceConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//        System.setProperty("spring.profiles.active","dev");
//        servletContext.setInitParameter("spring.profiles.active","dev");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.getEnvironment().setActiveProfiles("production");
        context.register(AccountConfig.class);
//        context.register(ProductionDataSourceConfig.class);
        context.setServletContext(servletContext);

        Car car = context.getBean(Car.class);
        System.out.println(car);
    }
}
