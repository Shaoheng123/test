package com.example.demo.config;

import com.example.demo.dao.AccountRepository;
import com.example.demo.Components.AccountComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:value.properties")
public class AccountConfig {

    @Autowired
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //Inversion of Control
//    @Bean
//    public AccountService accountService() {
//        return new AccountService(accountRepository());
//    }
    @Bean
    public AccountComponent accountComponent() {
        return new AccountComponent();
    }


    @Bean
    @Profile("production")
    public DatasourceConfig productionDataSourceConfig() {
        return new ProductionDataSourceConfig();
    }

    @Bean
    @Profile("dev")
    public DatasourceConfig devDataSourceConfig() {
        return new DevDataSourceConfig();
    }

    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository();
    }
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("config/messages");
//        return messageSource;
//    }


}
