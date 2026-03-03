package com.stablishmentservice.stablishmentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratedRamdonCode {

    private final StablishmentRepository stablishmentRepository;

    public String generateUniqueCode(String name) {

        String prefix = name.trim().length() >= 3
                ? name.substring(0, 3).toUpperCase()
                : name.toUpperCase();

        List<String> codes = stablishmentRepository.findLastCodeByPrefix(prefix);

        int nextNumber = 1;

        if (!codes.isEmpty()) {
            String lastCode = codes.get(0);
            String[] parts = lastCode.split("-");
            try {
                nextNumber = Integer.parseInt(parts[1]) + 1;
            } catch (Exception ignored) {
                nextNumber = 1;
            }
        }

        return String.format("%s-%04d", prefix, nextNumber);
    }
}
