package com.example.capstoneMap.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello from Spring Boot!";
    }
}