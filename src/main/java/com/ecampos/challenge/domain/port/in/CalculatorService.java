package com.ecampos.challenge.domain.port.in;

import com.ecampos.challenge.domain.model.CalculationResult;

import java.math.BigDecimal;

public interface CalculatorService {

    CalculationResult calculateWithPercentage(BigDecimal num1, BigDecimal num2);




}
