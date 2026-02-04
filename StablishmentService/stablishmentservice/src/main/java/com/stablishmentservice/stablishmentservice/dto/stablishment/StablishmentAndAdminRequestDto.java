package com.stablishmentservice.stablishmentservice.dto.stablishment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentAndAdminRequestDto {
    @Valid
    @NotNull
    private StablishmentRequestDto stablishment;
    @Valid
    @NotNull
    private AdminUserRequestDto adminUser;
}
