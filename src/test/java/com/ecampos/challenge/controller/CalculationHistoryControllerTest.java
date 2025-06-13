package com.ecampos.challenge.controller;

import com.ecampos.challenge.adapters.in.rest.controller.CalculationHistoryController;
import com.ecampos.challenge.domain.port.in.CalculationHistoryService;
import com.ecampos.challenge.adapters.in.rest.dto.CalculationHistoryDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculationHistoryController.class)
class CalculationHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculationHistoryService historyService;

    @Test
    void getCalculationHistory_returnsPageWithData() throws Exception {
        CalculationHistoryDto dto = new CalculationHistoryDto(
                1L,
                LocalDateTime.now(),
                "/ecampos/v1/calculator",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(33),
                null
        );

        Page<CalculationHistoryDto> mockPage = new PageImpl<>(List.of(dto));

        Mockito.when(historyService.getCalculationHistory(PageRequest.of(0, 10)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/ecampos/v1/log/calculation-history?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].number1").value(10));
    }

    @Test
    void getCalculationHistory_emptyPage_returnsEmptyArray() throws Exception {
        Page<CalculationHistoryDto> emptyPage = new PageImpl<>(List.of());

        Mockito.when(historyService.getCalculationHistory(PageRequest.of(0, 10)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/ecampos/v1/log/calculation-history?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}
