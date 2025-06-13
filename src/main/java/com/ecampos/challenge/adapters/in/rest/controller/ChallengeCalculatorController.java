package com.ecampos.challenge.adapters.in.rest.controller;


import com.ecampos.challenge.adapters.in.rest.dto.CalculationResponse;
import com.ecampos.challenge.domain.model.CalculationResult;
import com.ecampos.challenge.domain.port.in.CalculatorService;
import com.ecampos.challenge.adapters.in.rest.dto.CalculationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ecampos/v1/calculator")
@RequiredArgsConstructor
public class ChallengeCalculatorController{


    private final CalculatorService calculatorService;
    /**
     * Endpoint principal: suma dos números y aplica el porcentaje dinámico.
     *
     * @param request JSON con num1 y num2
     * @return Resultado del cálculo con porcentaje aplicado
     */
    @PostMapping
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest request) {
        CalculationResult result = calculatorService.calculateWithPercentage(request.getNum1(), request.getNum2());

        CalculationResponse response = CalculationResponse.builder()
                .timestamp(LocalDateTime.now())
                .originalSum(result.getBaseSum())
                .percentageApplied(result.getPercentageApplied())
                .finalResult(result.getFinalResult())
                .build();

        return ResponseEntity.ok(response);
    }


}


