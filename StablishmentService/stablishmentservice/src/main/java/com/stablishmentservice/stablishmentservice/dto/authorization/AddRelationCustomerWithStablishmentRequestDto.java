package com.stablishmentservice.stablishmentservice.dto.authorization;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRelationCustomerWithStablishmentRequestDto {
    @NotBlank(message = "stablishmentCode is required")
    private String stablishmentCode;
}
