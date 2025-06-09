package com.filmrental.mapper;

import com.filmrental.model.dto.PaymentRequestDTO;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Payment;
import com.filmrental.model.entity.Rental;
import com.filmrental.model.entity.Staff;

public class PaymentMapper {

    public static PaymentRequestDTO toDto(Payment payment) {
        if (payment == null) return null;

        return new PaymentRequestDTO(
                payment.getPaymentId(),
                payment.getCustomer() != null ? payment.getCustomer().getCustomerId() : null,
                payment.getStaff() != null ? payment.getStaff().getStaffId() : null,
                payment.getRental() != null ? payment.getRental().getRentalId() : null,
                payment.getAmount(),
                payment.getPaymentDate() != null ? payment.getPaymentDate().toLocalDate() : null,
                payment.getLastUpdate()
        );
    }

    public static Payment toEntity(PaymentRequestDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());

        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        payment.setCustomer(customer);

        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        payment.setStaff(staff);

        Rental rental = new Rental();
        rental.setRentalId(dto.getRentalId());
        payment.setRental(rental);

        payment.setAmount(dto.getAmount());
        payment.setPaymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate().atStartOfDay() : null);
        payment.setLastUpdate(dto.getLastUpdate());
        return payment;
    }
}