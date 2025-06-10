package com.ecampos.challenge.application.service;


import com.ecampos.challenge.domain.service.CalculationHistoryService;
import com.ecampos.challenge.domain.service.HistoryLoggerPort;
import com.ecampos.challenge.dto.CalculationHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculationHistoryServiceImpl implements CalculationHistoryService {

    private final HistoryLoggerPort calculationHistoryPort;

    @Override
    public Page<CalculationHistoryDto> getCalculationHistory(Pageable pageable) {
        return calculationHistoryPort.getCalculationHistory(pageable);
    }
}