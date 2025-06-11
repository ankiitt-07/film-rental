package com.filmrental.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Integer paymentId;
    private Integer customerId;
    private Integer staffId;
    private Integer rentalId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
}