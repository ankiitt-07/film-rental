package com.filmrental.controller;

import com.filmrental.mapper.PaymentMapper;
import com.filmrental.model.dto.PaymentRequestDTO;
import com.filmrental.model.entity.Payment;
import com.filmrental.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @PutMapping("/add")
    public ResponseEntity<String> addPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        Payment payment = PaymentMapper.toEntity(paymentRequest);
        paymentRepository.save(payment);
        return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);
    }
}