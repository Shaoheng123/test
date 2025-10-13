package com.example.demo;

import com.example.demo.Components.AccountComponent;
import com.example.demo.Entity.Car;
import com.example.demo.Entity.Engine;
import com.example.demo.Entity.Transmission;
import com.example.demo.Service.AccountService;
import com.example.demo.config.AccountConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@SpringBootTest(classes = {AccountComponent.class})
class DemoApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccountConfig accountConfig;

    @Autowired
    private  AccountComponent accountComponent;

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
//        assertTrue(car,car.getCarId(),car);
    }
    @Test
    void testValueInjection(){
//        ApplicationContext context = new AnnotationConfigApplicationContext(AccountConfig.class);
//        AccountComponent accountComponent = context.getBean(AccountComponent.class);
        Assertions.assertEquals(accountComponent.valueMapKey1,Integer.valueOf(1));

    }
    @Test
    void getApplicationContext() {
        ApplicationContext context = applicationContext;
        Assertions.assertEquals(context,accountConfig.getApplicationContext());
    }

}
