package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.service.stablishment.StablishmentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/stablishments")
@RequiredArgsConstructor
public class StablishmentController {

    private final StablishmentService stablishmentService;

    @GetMapping
    public ResponseEntity<List<Stablishment>> getMethodName() {
        return ResponseEntity.ok(stablishmentService.getAllStablishments());
    }
    // Stablishment endpoints
    @PostMapping("/create-stablishment")
    public ResponseEntity<StablishmentAndAdminResponseDto> createStablishment(@RequestBody StablishmentAndAdminRequestDto stablishmentAndAdminRequestDto) {
        StablishmentAndAdminResponseDto response = stablishmentService.createStablishmentWithAdmin(stablishmentAndAdminRequestDto.getStablishment(), stablishmentAndAdminRequestDto.getAdminUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-stablishment/{id}")
    public ResponseEntity<Void> putMethodName(@PathVariable Long id, @RequestBody StablishmentRequestDto body) {
        stablishmentService.updateStablishment(body, id);
        return ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-stablishment")
    public ResponseEntity<Void> deleteStablishment() {
        stablishmentService.deleteStablishment();
        return ResponseEntity.ok().build();
    }
    // TODO: create methods gets by different parameters
    // Get stablishment code by user id
    @GetMapping("/get-stablishment-code/{userId}")
    public ResponseEntity<String> getStablishmentCodeByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(stablishmentService.getStablishmentCodeByUserId(userId));
    }
    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping("/get-stablishment-by-token")
    // public ResponseEntity<StablishmentRequestDto> getStablishmentByToken(@RequestParam String param) {
        
    // }
    
    
    // necesitamos
    // datos del establecimiento
    // datos del usuario que lo crea
    
    
    

}
