package ru.mfa.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello_controller {

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }
}