package com.ecampos.challenge.domain.model;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@Getter
@Setter
@Data
public class CalculationResult {
    private BigDecimal baseSum;
    private BigDecimal percentageApplied;
    private BigDecimal finalResult;
}
