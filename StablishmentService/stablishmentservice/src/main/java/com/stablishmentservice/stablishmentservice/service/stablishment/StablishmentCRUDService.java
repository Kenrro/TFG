package com.stablishmentservice.stablishmentservice.service.stablishment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;
import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StablishmentCRUDService {
        private final StablishmentRepository stablishmentRepository;

    public List<Stablishment> findAll() {
        return stablishmentRepository.findAll();
    }

    public Stablishment findById(Long id) {
        return stablishmentRepository.findById(id)
            .orElseThrow(() ->
                new StablishmentGeneralException(
                    StablishmentError.STABLISHMENT_NOT_FOUND
                )
            );
    }

    public Stablishment findByCode(String code) {
        return stablishmentRepository.findByCode(code)
            .orElseThrow(() ->
                new StablishmentGeneralException(
                    StablishmentError.STABLISHMENT_NOT_FOUND
                )
            );
    }

    public Stablishment create(Stablishment stablishment) {
        return stablishmentRepository.save(stablishment);
    }

    public Stablishment update(Long id, StablishmentRequestDto dto) {
        Stablishment stablishment = findById(id);

        stablishment.setName(dto.getName());
        stablishment.setAddress(dto.getAddress());
        stablishment.setDescription(dto.getDescription());

        return stablishment;
    }

    public void delete(Long id) {
        Stablishment stablishment = findById(id);
        stablishmentRepository.delete(stablishment);
    }
    public List<Stablishment> findByIds(List<Long> ids) {
        return stablishmentRepository.findAllByIds(ids);
    }
}
