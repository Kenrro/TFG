package com.authservice.auth.dto.stablisment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollbackDeleteEmployeesRequestDto {
    private List<RollbackEmployeeDto> employees;
}
