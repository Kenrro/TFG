package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.authorization.AddRelationUserWithStablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.service.UserStablishmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/user-stablishments")
@RequiredArgsConstructor
public class UserStablishmentController {
    
    final private UserStablishmentService userStablishmentService;

    @PostMapping("/add-relation-user-stablishment")
    public ResponseEntity<Void> addRelationUserWithStablishment(@RequestBody AddRelationUserWithStablishmentRequestDto requestDto) {

        userStablishmentService.createUserStablishmentRelation(requestDto.getUserId(), requestDto.getStablishmentCode());
        return ResponseEntity.ok().build();
    
    }
    @DeleteMapping("/delete-relations-by-stablishment/{userId}")
    public ResponseEntity<Void> deleteRelationsByStablishment(@PathVariable Long userId) {
        userStablishmentService.deleteUserStablishmentRelations(userId);
        return ResponseEntity.ok().build();
    }

    
}
