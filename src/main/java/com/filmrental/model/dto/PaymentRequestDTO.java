package com.filmrental.model.dto;

import lombok.*;
import org.hibernate.id.IntegralDataTypeHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDTO {

    private Integer paymentId;
    private Integer customerId;
    private Integer staffId;
    private Integer rentalId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    LocalDateTime lastUpdate;

}