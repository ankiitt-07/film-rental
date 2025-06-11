package com.filmrental.mapper;

import com.filmrental.model.dto.PaymentDTO;
import com.filmrental.model.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setCustomerId(payment.getCustomer() != null ? payment.getCustomer().getCustomerId() : null);
        dto.setStaffId(payment.getStaff() != null ? payment.getStaff().getStaffId() : null);
        dto.setRentalId(payment.getRental() != null ? payment.getRental().getRentalId() : null);
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }

    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate());
        return payment;
    }
}