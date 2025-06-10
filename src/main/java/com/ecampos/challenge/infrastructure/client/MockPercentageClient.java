package com.ecampos.challenge.infrastructure.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockPercentageClient implements PercentageClient{

    @Override
    public BigDecimal getPercentage() {
        if (Math.random() < 0.5) {
            throw new RuntimeException("Error simulado del servicio externo");
        }
        return new BigDecimal(Math.random()*100);
    }
}
