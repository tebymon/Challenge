package com.ecampos.challenge.infrastructure.persistence;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "calculation_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String endpoint;

    private BigDecimal number1;

    private BigDecimal number2;

    private BigDecimal percentageUsed;

    private String response; // Puede ser resultado o mensaje de error
}
