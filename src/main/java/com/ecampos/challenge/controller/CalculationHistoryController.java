package com.ecampos.challenge.controller;


import com.ecampos.challenge.domain.service.CalculationHistoryService;
import com.ecampos.challenge.dto.CalculationHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ecampos/v1/log")
@RequiredArgsConstructor
public class CalculationHistoryController {

    private final CalculationHistoryService service;

    @GetMapping("/calculation-history")
    public Page<CalculationHistoryDto> getCalculationHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getCalculationHistory(PageRequest.of(page, size));
    }

}
