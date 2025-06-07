package com.filmrental.mapper;

import com.filmrental.model.dto.PaymentRequestDTO;
import com.filmrental.model.entity.Payment;
import java.time.LocalDateTime;

public class PaymentMapper {

    public static Payment toEntity(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setCustomerId(dto.getCustomerId());
        payment.setStaffId(dto.getStaffId());
        payment.setRentalId(dto.getRentalId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setLastUpdate(LocalDateTime.now());
        return payment;
    }
}