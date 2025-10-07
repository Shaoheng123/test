package com.example.demo;

import com.example.demo.Entity.Car;
import com.example.demo.Entity.Engine;
import com.example.demo.Entity.Transmission;
import com.example.demo.RestController.TestRestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Test
	void testAccountServiceBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AccountConfig.class);
        AccountService accountService = context.getBean(AccountService.class);
        assertNotNull(accountService);

	}

    @Test
    void testGetMessage() {
        String key = "account.create.success";
        String message = "Account successfully created!";
        System.out.println("hello");
        Assertions.assertNotEquals("account.create.success", key);
    }
    void testCreateCar() {
        Engine engine = new Engine();
        engine.setEngineId(10L);
        engine.setEngineType("Hello");
        Transmission transmission = new Transmission();


        Car car = new Car(engine,transmission,0L);
        assertTrue(car,car.getCarId(),cae);
    }

}
