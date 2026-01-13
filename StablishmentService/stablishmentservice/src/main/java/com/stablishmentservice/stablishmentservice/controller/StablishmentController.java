package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminRequestDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.service.StablishmentService;

import lombok.RequiredArgsConstructor;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/stablishments")
@RequiredArgsConstructor
public class StablishmentController {
    // TODO: create user entity,
    // controllers for add users to stablishments,
    // create dtos
    // more endpoints
    // user controller

    private final StablishmentService stablishmentService;

    @GetMapping
    public ResponseEntity<List<Stablishment>> getMethodName() {
        return ResponseEntity.ok(stablishmentService.getAllStablishments());
    }
    @PostMapping("/create-stablishment")
    public ResponseEntity<Void> createStablishment(@RequestBody StablishmentAndAdminRequestDto stablishmentAndAdminRequestDto) {
        stablishmentService.createStablishmentWithAdmin(stablishmentAndAdminRequestDto.getStablishment(), stablishmentAndAdminRequestDto.getAdminUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // necesitamos
    // datos del establecimiento
    // datos del usuario que lo crea
    
    
    

}
