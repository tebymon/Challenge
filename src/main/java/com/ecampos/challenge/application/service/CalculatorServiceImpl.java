package com.ecampos.challenge.application.service;

import com.ecampos.challenge.domain.model.CalculationResult;
import com.ecampos.challenge.domain.service.CalculatorService;
import com.ecampos.challenge.domain.service.HistoryLoggerPort;
import com.ecampos.challenge.exception.InvalidRequestException;
import com.ecampos.challenge.exception.ProcessingException;
import com.ecampos.challenge.infrastructure.client.PercentageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculatorServiceImpl implements CalculatorService {

    private final PercentageClient percentageClient;
    private final HistoryLoggerPort historyLoggerPort;
    private static final String CACHE_NAME = "percentage-cache";
    private static final String CACHE_KEY = "current-percentage";
    private static final BigDecimal ONE_HUNDRED= BigDecimal.valueOf(100);

    @Override
    @Retry(name = "${tenpo.percentage.retry-name:percentageRetry}", fallbackMethod = "useCachedPercentage")
    public CalculationResult calculateWithPercentage(BigDecimal num1, BigDecimal num2) {
        validateNotNull(num1, "num1");
        validateNotNull(num2, "num2");
        BigDecimal base = num1.add(num2);
        BigDecimal percentage = BigDecimal.ZERO; // valor por defecto
        CalculationResult response;

        try {
            percentage = percentageClient.getPercentage();
        } catch (Exception e) {
            // Se atrapa el error, pero no se lanza. Solo se registra luego en el log asíncrono
            response = new CalculationResult(base, percentage, BigDecimal.ZERO); // resultado con porcentaje 0
            logCallAsync(num1, num2, response.getFinalResult(), String.valueOf(e));
             throw new ProcessingException("No se pudo procesar el cálculo: " + e.getMessage());
        }

        BigDecimal result = base.add(base.multiply(percentage).divide(ONE_HUNDRED));
        response = new CalculationResult(base, percentage, result);

        logCallAsync(num1, num2, response.getFinalResult(), null);
        return response;
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidRequestException("El campo " + fieldName + " no puede ser nulo.");
        }
    }

    /*
    private double fetchPercentageFromService() {
        double percentage = percentageClient.getPercentage(); // Puede fallar
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(CACHE_KEY, percentage);
        }
        return percentage;
    }

    public CalculationResult useCachedPercentage(double num1, double num2, Throwable t) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Double cached = cache.get(CACHE_KEY, Double.class);
            if (cached != null) {
                double base = num1 + num2;
                double result = base + (base * cached / 100);
                CalculationResult fallbackResult = new CalculationResult(base, cached, result);

                logCallAsync(num1, num2, fallbackResult, "Usando valor en caché por error: " + t.getMessage());
                return fallbackResult;
            }
        }private BigDecimal number1;

        logCallAsync(num1, num2, null, "Error sin porcentaje válido: " + t.getMessage());
        throw new PercentageUnavailableException("No se pudo obtener el porcentaje y no hay valor en caché");
    }
*/
    @Async
    public void logCallAsync(BigDecimal num1, BigDecimal num2, BigDecimal result, String error) {
        try {
            log.info("INI generacion de log.");
            historyLoggerPort.logCalculation(
                    LocalDateTime.now(),
                    "/api/v1/calculate",
                    num1,
                    num2,
                    result,
                    error
            );
        } catch (Exception e) {
            // Silencioso: no debe impactar la ejecución
            System.err.println("Fallo registrando historial: " + e.getMessage());
        }
    }
}
