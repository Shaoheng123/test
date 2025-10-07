package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class AccountService {
//    inject repository and messageSOurce
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageSource messageSource;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public String getMessage(String key) {
        return messageSource.getMessage(key,null, Locale.getDefault());
    }

    public void createAccount() {}
}
