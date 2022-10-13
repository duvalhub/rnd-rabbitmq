package com.duvalhub.rnd.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Receiver {

    public String receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        return "Hell yeah brother!";
    }
}
