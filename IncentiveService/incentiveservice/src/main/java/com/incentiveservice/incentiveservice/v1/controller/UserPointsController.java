package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UsersPointsDto;
import com.incentiveservice.incentiveservice.v1.service.userpoints.UserPointsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1/user-points")
@RequiredArgsConstructor
public class UserPointsController {

    private final UserPointsService userPointsService;

    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping
    public ResponseEntity<Void> createUserPoints(@RequestBody UserPointsCreateRequestDto request) {
        System.out.println("ENTRA");
        userPointsService.create(request.getUserId(), request.getStablishmentCode());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/{userId}/{stablishmentCode}")
    public ResponseEntity<UserPointsDto> getUserPoints(
            @PathVariable Long userId,
            @PathVariable String stablishmentCode
    ) {
        UserPointsDto userPointsResponseDto =
                userPointsService.getUserPointsByUserIdAndStablishmentCode(userId, stablishmentCode);
        return ResponseEntity.ok(userPointsResponseDto);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @PutMapping("/rest")
    public ResponseEntity<Void> subtractPoints(@RequestBody UserPointsUpdateRequestDto request) {
        userPointsService.subtractPoints(request.getUserId(), request.getStablishmentCode(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('SERVICE')")
    @PutMapping("/sum")
    public ResponseEntity<Void> addPoints(@RequestBody UserPointsUpdateRequestDto request) {
        userPointsService.addPoints(request.getUserId(), request.getStablishmentCode(), request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/{userId}/{stablishmentCode}/delete")
    public ResponseEntity<UsersPointsDto> deleteUserPoints(
            @PathVariable Long userId,
            @PathVariable String stablishmentCode
    ) {
        UsersPointsDto usersPointsDto = userPointsService.deleteUserPoints(userId, stablishmentCode);
        return ResponseEntity.ok(usersPointsDto);
    }
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-all-user-wallets/{userId}")
    public ResponseEntity<UsersPointsDto> deleteAllUserPoints(
            @PathVariable Long userId
    ) {
        UsersPointsDto usersPointsResponseDto = userPointsService.deleteAllUserPointsByUserId(userId);
        return ResponseEntity.ok(usersPointsResponseDto);
    }
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-all-users-wallets-by-stablishment-code/{stablishmentCode}")
    public ResponseEntity<UsersPointsDto> deleteAllUserPoidnts(
            @PathVariable String stablishmentCode
    ) {
        UsersPointsDto usersPointsResponseDto = userPointsService.deleteAllUserPointsByStablishmentCode(stablishmentCode);
        return ResponseEntity.ok(usersPointsResponseDto);
    }

    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/rollback-delete")
    public ResponseEntity<Void> rollbackDelete(
        @RequestBody UsersPointsDto request
    ) {
        userPointsService.createAll(request);
        return ResponseEntity.ok().build();
    }
    
}
