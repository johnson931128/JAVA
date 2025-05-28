package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;
//http://localhost:8080/

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/user")
    public Map<String, String> getUser() {
        Map<String, String> user = new HashMap<>();
        user.put("name", "范舜傑");
        user.put("university", "成功大學");
        user.put("department", "工程科學系");
        return user;
    }

    @GetMapping("/read")
    public String Read() {
        return "WTF is this tool?";
    }
    
}

