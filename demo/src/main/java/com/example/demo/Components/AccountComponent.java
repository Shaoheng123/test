package com.example.demo.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class AccountComponent {

    @Value("#{${valueMap}['key2']}")
    public Integer valueMapKey1;

    public Integer getValueMapKey1() {
        return valueMapKey1;
    }


}
