package com.authservice.auth.dto.stablisment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRelationUserWithStablishmentRequestDto {
    private Long userId;
    private String StablishmentCode;
}
