package com.stablishmentservice.stablishmentservice.dto.stablishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentAndAdminRequestDto {
    private StablishmentRequestDto stablishment;
    private AdminUserRequestDto adminUser;
}
