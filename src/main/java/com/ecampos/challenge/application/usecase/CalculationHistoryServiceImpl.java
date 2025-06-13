package com.ecampos.challenge.application.usecase;


import com.ecampos.challenge.domain.port.in.CalculationHistoryService;
import com.ecampos.challenge.domain.port.out.HistoryLoggerPort;
import com.ecampos.challenge.adapters.in.rest.dto.CalculationHistoryDto;
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