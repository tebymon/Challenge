package com.ecampos.challenge.domain.service;


import com.ecampos.challenge.dto.CalculationHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalculationHistoryService {
    Page<CalculationHistoryDto> getCalculationHistory(Pageable pageable);
}