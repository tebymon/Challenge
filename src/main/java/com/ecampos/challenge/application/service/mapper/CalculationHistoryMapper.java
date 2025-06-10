package com.ecampos.challenge.application.service.mapper;


import com.ecampos.challenge.dto.CalculationHistoryDto;
import com.ecampos.challenge.infrastructure.persistence.CalculationHistoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CalculationHistoryMapper {

    public CalculationHistoryDto toDomain(CalculationHistoryEntity entity) {
        CalculationHistoryDto domain = new CalculationHistoryDto();
        domain.setId(entity.getId());
        domain.setTimestamp(entity.getTimestamp());
        domain.setEndpoint(entity.getEndpoint());
        domain.setNumber1(entity.getNumber1());
        domain.setNumber2(entity.getNumber2());
        domain.setPercentageUsed(entity.getPercentageUsed());
        domain.setResponse(entity.getResponse());
        return domain;
    }
}
