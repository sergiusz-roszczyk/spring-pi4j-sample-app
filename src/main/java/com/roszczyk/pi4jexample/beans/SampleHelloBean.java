package com.roszczyk.pi4jexample.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleHelloBean {

    @Value("Wstrzyknięte Witaj Świecie!")
    private String message;

    public String getMessage() {
        return message;
    }
}
