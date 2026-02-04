package com.transactionservice.transactionservice.v1.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Test {
    @GetMapping("/test")
    public String getMethodName() {
        return "Hola mundo";
    }
    
}
