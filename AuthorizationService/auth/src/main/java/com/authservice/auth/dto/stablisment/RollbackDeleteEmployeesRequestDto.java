package com.authservice.auth.dto.stablisment;

import java.util.List;


import lombok.Data;

@Data
public class RollbackDeleteEmployeesRequestDto {
    private List<RollbackEmployeeDto> employees;
}
