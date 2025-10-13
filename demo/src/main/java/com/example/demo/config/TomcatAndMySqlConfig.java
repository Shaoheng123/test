package com.example.demo.config;

public class TomcatAndMySqlConfig implements DatasourceConfig{

    @Override
    public void setup() {
        System.out.println("TomCat And Mysql");
    }
}
