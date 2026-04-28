package ru.mfa.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private List<String> users = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));

    // GET: получить всех пользователей
    @GetMapping
    public List<String> getAllUsers() {
        return users;
    }

    // POST: добавить нового пользователя
    @PostMapping
    public String addUser(@RequestBody String name) {
        users.add(name);
        return "User " + name + " added!";
    }

    // DELETE: удалить пользователя
    @DeleteMapping("/{name}")
    public String deleteUser(@PathVariable String name) {
        if (users.remove(name)) {
            return "User " + name + " deleted!";
        }
        return "User not found!";
    }
}