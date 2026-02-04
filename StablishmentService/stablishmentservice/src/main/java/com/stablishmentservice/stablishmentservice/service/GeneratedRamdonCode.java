package com.stablishmentservice.stablishmentservice.service;

import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratedRamdonCode {
    final private StablishmentRepository stablishmentRepository;

    public String generateUniqueCode(String name) {
        String prefix = name.trim().length() >= 3 ? name.substring(0, 3).toUpperCase()
                                           : name.toUpperCase();

        // Buscar el último código con este prefijo
        String lastCode = stablishmentRepository.findLastCodeByPrefix(prefix);

        int nextNumber = 1; // si no hay ninguno, empezamos en 1
        if (lastCode != null && lastCode.contains("-")) {
            String[] parts = lastCode.split("-");
            try {
                nextNumber = Integer.parseInt(parts[1]) + 1;
            } catch (NumberFormatException e) {
                // fallback por si hay código corrupto
                nextNumber = 1;
            }
        }

        // Formatear con 4 dígitos: 0001, 0002...
        return String.format("%s-%04d", prefix, nextNumber);
    }
}
