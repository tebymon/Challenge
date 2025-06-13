package com.ecampos.challenge.application.usecase;

import com.ecampos.challenge.domain.model.CalculationResult;
import com.ecampos.challenge.domain.port.in.CalculatorService;
import com.ecampos.challenge.domain.port.out.HistoryLoggerPort;
import com.ecampos.challenge.infrastructure.exception.InvalidRequestException;
import com.ecampos.challenge.infrastructure.exception.ProcessingException;
import com.ecampos.challenge.adapters.out.client.PercentageClient;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.cache.Cache;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculatorServiceImpl implements CalculatorService {

    private final PercentageClient percentageClient;
    private final HistoryLoggerPort historyLoggerPort;
    private final CacheManager cacheManager;
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

        log.info("Inicio cálculo con porcentaje para num1={}, num2={}", num1, num2);

        try {
            percentage = fetchPercentageWithRetry();
            log.info("Porcentaje obtenido desde servicio: {}", percentage);
        } catch (Exception e) {
            log.error("Error al obtener porcentaje desde servicio después de retry: {}", e.toString());
            response = new CalculationResult(base, percentage, BigDecimal.ZERO); // resultado con porcentaje 0
            logCallAsync(num1, num2, response.getFinalResult(), String.valueOf(e));
            throw new ProcessingException("No se pudo procesar el cálculo: " + e.getMessage());
        }

        BigDecimal result = base.add(base.multiply(percentage).divide(ONE_HUNDRED));
        response = new CalculationResult(base, percentage, result);

        log.info("Cálculo finalizado. Resultado: {}", result);
        logCallAsync(num1, num2, response.getFinalResult(), null);
        return response;
    }


    private BigDecimal fetchPercentageWithRetry() {
        log.info("Solicitando porcentaje al cliente externo con retry");
        BigDecimal percentage = percentageClient.getPercentage();
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            try {
                cache.put(CACHE_KEY, percentage);
                log.info("Porcentaje guardado en cache");
            } catch (Exception e) {
                log.warn("No se pudo guardar porcentaje en caché: {}", e.getMessage());
                throw new ProcessingException("No se pudo guardar porcentaje en caché: "+ e.getMessage());
            }
        } else {
            log.warn("Cache '{}' no disponible", CACHE_NAME);
        }
        return percentage;
    }
    private CalculationResult useCachedPercentage(Throwable t) {
        // Este fallback es llamado solo por el retry en fetchPercentageWithRetry
        log.warn("Fallback: usando porcentaje cacheado debido a error: {}", t.getMessage());
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            BigDecimal cached = cache.get(CACHE_KEY, BigDecimal.class);
            if (cached != null) {
                log.info("Porcentaje cacheado usado en fallback: {}", cached);
                // Para usar este valor en el cálculo, habría que modificar el flujo,
                // pero el fallback está en fetchPercentageWithRetry que retorna BigDecimal.
                // Por eso el fallback retorna el porcentaje cacheado, no CalculationResult.
                return new CalculationResult(null, cached, null); // Solo ejemplo, manejar en el caller
            } else {
                log.warn("No se encontró valor cacheado para fallback");
            }
        } else {
            log.warn("Cache '{}' no disponible para fallback", CACHE_NAME);
        }

        throw new ProcessingException("No se pudo procesar el cálculo y no hay porcentaje válido en cache");
    }

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

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidRequestException("El campo " + fieldName + " no puede ser nulo.");
        }
    }

}
