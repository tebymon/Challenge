package com.ecampos.challenge.controller;

import com.ecampos.challenge.adapters.in.rest.controller.ChallengeCalculatorController;
import com.ecampos.challenge.domain.model.CalculationResult;
import com.ecampos.challenge.domain.port.in.CalculatorService;
import com.ecampos.challenge.adapters.in.rest.dto.CalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChallengeCalculatorController.class)
class ChallengeCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private CalculationRequest validRequest;

    @BeforeEach
    void setup() {
        validRequest = new CalculationRequest();
        validRequest.setNum1(BigDecimal.valueOf(10));
        validRequest.setNum2(BigDecimal.valueOf(20));
    }

    @Test
    void calculate_HappyPath_ReturnsValidResponse() throws Exception {
        // Arrange
        CalculationResult result = new CalculationResult(
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(33)
        );

        Mockito.when(calculatorService.calculateWithPercentage(
                        BigDecimal.valueOf(10), BigDecimal.valueOf(20)))
                .thenReturn(result);

        // Act & Assert
        mockMvc.perform(post("/ecampos/v1/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalSum").value(30))
                .andExpect(jsonPath("$.percentageApplied").value(10))
                .andExpect(jsonPath("$.finalResult").value(33));
    }

    @Test
    void calculate_InvalidRequest_ReturnsBadRequest() throws Exception {
        CalculationRequest invalidRequest = new CalculationRequest(); // ambos nulos

        mockMvc.perform(post("/ecampos/v1/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
