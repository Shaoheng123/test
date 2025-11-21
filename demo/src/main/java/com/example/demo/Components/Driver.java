package com.example.demo.Components;

import com.example.demo.Entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Driver {
    @Autowired
    public Vehicle vehicle;

    @Autowired
    private Biker biker;

    public Driver() {
        System.out.println("Driver Singleton created");
    }

    public Biker getPrototypeBiker() {
        return biker;
    }

}
