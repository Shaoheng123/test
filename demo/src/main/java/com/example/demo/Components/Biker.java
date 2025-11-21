package com.example.demo.Components;

import com.example.demo.Entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Biker {
    @Autowired
    @Qualifier("bike")
    public Vehicle vehicle;

    public Biker(){
        System.out.println("Biker ProtoType Created");
    }




}
