package com.filmrental.mapper;

import com.filmrental.model.dto.PaymentRequestDTO;
import com.filmrental.model.entity.Payment;

public class PaymentMapper {

    public static PaymentRequestDTO toDto(Payment payment) {
        if (payment == null) return null;

        return new PaymentRequestDTO(
                payment.getPaymentId(),
                payment.getCustomerId(),
                payment.getStaffId(),
                payment.getRentalId(),
                payment.getAmount(),
                payment.getPaymentDate() != null ? payment.getPaymentDate().toLocalDate() : null,
                payment.getLastUpdate()
        );
    }

    public static Payment toEntity(PaymentRequestDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setCustomerId(dto.getCustomerId());
        payment.setStaffId(dto.getStaffId());
        payment.setRentalId(dto.getRentalId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate().atStartOfDay() : null);
        payment.setLastUpdate(dto.getLastUpdate());
        return payment;
    }
}