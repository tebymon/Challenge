package com.ecampos.challenge.domain.port.in;


import com.ecampos.challenge.adapters.in.rest.dto.CalculationHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalculationHistoryService {
    Page<CalculationHistoryDto> getCalculationHistory(Pageable pageable);
}