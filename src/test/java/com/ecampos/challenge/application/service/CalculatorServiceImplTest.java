package com.ecampos.challenge.application.service;

import com.ecampos.challenge.domain.model.CalculationResult;
import com.ecampos.challenge.application.usecase.CalculatorServiceImpl;
import com.ecampos.challenge.domain.port.out.HistoryLoggerPort;
import com.ecampos.challenge.infrastructure.exception.InvalidRequestException;
import com.ecampos.challenge.infrastructure.exception.ProcessingException;
import com.ecampos.challenge.adapters.out.client.PercentageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorServiceImplTest {

    @Mock
    private PercentageClient percentageClient;

    @Mock
    private HistoryLoggerPort historyLoggerPort;

    private Cache cache;
    private ConcurrentMapCacheManager cacheManager;

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("percentage-cache");
        calculatorService = new CalculatorServiceImpl(percentageClient, historyLoggerPort, cacheManager);
        cache = cacheManager.getCache("percentage-cache");
    }

    // --- HAPPY PATH ---

    @Test
    void testCalculateWithPercentage_success() {
        BigDecimal num1 = BigDecimal.valueOf(100);
        BigDecimal num2 = BigDecimal.valueOf(50);
        BigDecimal mockPercentage = BigDecimal.valueOf(10); // 10%

        when(percentageClient.getPercentage()).thenReturn(mockPercentage);

        CalculationResult result = calculatorService.calculateWithPercentage(num1, num2);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), result.getBaseSum());
        assertEquals(mockPercentage, result.getPercentageApplied());

        BigDecimal expectedFinalResult = BigDecimal.valueOf(150)
                .add(BigDecimal.valueOf(150).multiply(mockPercentage).divide(BigDecimal.valueOf(100)));
        assertEquals(expectedFinalResult, result.getFinalResult());

        // Verifica que se haya guardado en cache
        BigDecimal cached = cache.get("current-percentage", BigDecimal.class);
        assertEquals(mockPercentage, cached);
    }

    // --- NON-HAPPY PATHS ---

    @Test
    void testCalculateWithPercentage_nullNum1_throwsException() {
        BigDecimal num1 = null;
        BigDecimal num2 = BigDecimal.TEN;

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                calculatorService.calculateWithPercentage(num1, num2)
        );

        assertEquals("El campo num1 no puede ser nulo.", exception.getMessage());
    }

    @Test
    void testCalculateWithPercentage_nullNum2_throwsException() {
        BigDecimal num1 = BigDecimal.TEN;
        BigDecimal num2 = null;

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                calculatorService.calculateWithPercentage(num1, num2)
        );

        assertEquals("El campo num2 no puede ser nulo.", exception.getMessage());
    }

    @Test
    void testCalculateWithPercentage_externalServiceFails_andNoCache_throwsProcessingException() {
        BigDecimal num1 = BigDecimal.TEN;
        BigDecimal num2 = BigDecimal.TEN;

        // Simula fallo en el servicio externo
        when(percentageClient.getPercentage()).thenThrow(new RuntimeException("Service unavailable"));

        ProcessingException exception = assertThrows(ProcessingException.class, () ->
                calculatorService.calculateWithPercentage(num1, num2)
        );

        assertTrue(exception.getMessage().contains("No se pudo procesar el cálculo"));
    }

}


