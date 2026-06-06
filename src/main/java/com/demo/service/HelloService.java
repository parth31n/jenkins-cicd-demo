package com.demo.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, World!";
        }
        return "Hello, " + name + "!";
    }

    public String getAppInfo() {
        return "Jenkins CI/CD Demo App - Spring Boot v3";
    }
}
