package com.example.demo.Entity;

import org.springframework.beans.factory.annotation.Autowired;

public class Car {
    Engine engine;
    Transmission transmission;
    Long carId;
    @Autowired
    public Car(Engine engine, Transmission transmission, Long carId) {
        this.engine = engine;
        this.carId = carId;
        this.transmission = transmission;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
}
