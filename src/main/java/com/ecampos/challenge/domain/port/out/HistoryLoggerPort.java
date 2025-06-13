package com.ecampos.challenge.domain.port.out;

import com.ecampos.challenge.adapters.in.rest.dto.CalculationHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface HistoryLoggerPort {
    void logCalculation(LocalDateTime timestamp, String endpoint, BigDecimal num1, BigDecimal num2, BigDecimal percentage, String responseOrError);
    Page<CalculationHistoryDto> getCalculationHistory(Pageable pageable);
}