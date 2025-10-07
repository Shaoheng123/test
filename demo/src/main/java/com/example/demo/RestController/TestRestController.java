package com.example.demo.RestController;

import com.example.demo.Entity.Model;
import com.example.demo.Entity.Scope;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {
    @Value("{$spring.application.name}")
    String appName;
    @Resource(name = "applicationScopedBean")
    Scope applicationScopedBean;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName",appName);
        return "home";
    }

    @GetMapping("/applicationscope")
    public String getApplicationScopeMessage(final Model model) {
        model.addAttribute("previousMessage", Scope.getMessage());
        applicationScopedBean.setMessage("Good afternoon!");
        model.addAttribute("currentMessage", applicationScopedBean.getMessage());
        return "scopesExample";


}
