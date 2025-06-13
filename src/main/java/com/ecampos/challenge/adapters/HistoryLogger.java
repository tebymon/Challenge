package com.ecampos.challenge.adapters;

import com.ecampos.challenge.application.usecase.mapper.CalculationHistoryMapper;
import com.ecampos.challenge.domain.port.out.HistoryLoggerPort;
import com.ecampos.challenge.adapters.in.rest.dto.CalculationHistoryDto;
import com.ecampos.challenge.adapters.out.persistence.CalculationHistoryEntity;
import com.ecampos.challenge.adapters.out.persistence.CalculationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryLogger implements HistoryLoggerPort {

    private final CalculationHistoryRepository repository;
    private final CalculationHistoryMapper mapper;

    @Override
    public void logCalculation(LocalDateTime timestamp, String endpoint,
                               BigDecimal num1, BigDecimal num2,
                               BigDecimal percentage, String responseOrError) {
        CalculationHistoryEntity history = CalculationHistoryEntity.builder()
                .timestamp(timestamp)
                .endpoint(endpoint)
                .number1(num1)
                .number2(num2)
                .percentageUsed(percentage)
                .response(responseOrError)
                .build();

        repository.save(history);
    }

    @Override
    public Page<CalculationHistoryDto> getCalculationHistory(Pageable pageable) {
        Page<CalculationHistoryEntity> entitiesPage = repository.findAllWithNativePagination(pageable);
        log.info(entitiesPage.toString());
        return entitiesPage.map(mapper::toDomain);
    }

}