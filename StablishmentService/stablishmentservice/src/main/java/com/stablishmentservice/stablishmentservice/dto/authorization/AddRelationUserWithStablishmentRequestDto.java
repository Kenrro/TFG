package com.stablishmentservice.stablishmentservice.dto.authorization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRelationUserWithStablishmentRequestDto {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "stablishmentCode is required")
    private String stablishmentCode;
}
