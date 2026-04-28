package ru.mfa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Time_controller {

    @GetMapping("/time")
    public String getCurrentTime() {
        // Получаем текущее локальное время
        LocalDateTime now = LocalDateTime.now();

        // Форматируем время в удобный вариант
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return "Current local date and time: " + now.format(formatter);
    }
}