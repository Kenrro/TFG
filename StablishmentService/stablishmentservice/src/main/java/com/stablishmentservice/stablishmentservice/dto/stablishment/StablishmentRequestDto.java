package com.stablishmentservice.stablishmentservice.dto.stablishment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentRequestDto {

    @NotBlank(message = "name is required")
    @Size(min = 2, max = 50, message = "name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "address is required")
    @Size(min = 5, max = 100, message = "address must be between 5 and 100 characters")
    private String address;

   
    @Size(max = 255, message = "description must be at most 255 characters")
    private String description;
}
