package com.ecampos.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalculationHistoryDto {
    private Long id;
    private LocalDateTime timestamp;
    private String endpoint;
    private BigDecimal number1;
    private BigDecimal number2;
    private BigDecimal percentageUsed;
    private String response;
}
