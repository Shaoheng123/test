package com.example.demo.NotUsed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class ProfileManager {
    @Autowired
    private Environment environment;
    public void getActiveProfiles() {
        for(String str : environment.getActiveProfiles()) {
            System.out.println(str);
        }
    }
}
