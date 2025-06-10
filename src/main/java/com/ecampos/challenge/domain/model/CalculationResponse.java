package com.ecampos.challenge.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CalculationResponse {

    private LocalDateTime timestamp;
    private BigDecimal originalSum;
    private BigDecimal percentageApplied;
    private BigDecimal finalResult;
}
