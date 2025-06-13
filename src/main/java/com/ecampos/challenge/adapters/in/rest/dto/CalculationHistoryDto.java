package com.ecampos.challenge.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationHistoryDto {
    private Long id;
    private LocalDateTime timestamp;
    private String endpoint;
    private BigDecimal number1;
    private BigDecimal number2;
    private BigDecimal percentageUsed;
    private String response;
}
