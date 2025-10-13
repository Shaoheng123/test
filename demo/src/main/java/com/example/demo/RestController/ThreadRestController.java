package com.example.demo.RestController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thread")
public class ThreadRestController {

    @Value("{$spring.application.name}")
    String appName;

    @GetMapping("/getVirtualThreadName")
    public String getThreadName() {
        return "IsVirtual :"+ Thread.currentThread().isVirtual();

    }

}
