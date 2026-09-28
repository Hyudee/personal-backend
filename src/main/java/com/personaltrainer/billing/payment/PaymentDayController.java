package com.personaltrainer.billing.payment;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/api/payment-days")
@RequiredArgsConstructor

public class PaymentDayController {

    private final PaymentDayRepository paymentDayRepository;

    @GetMapping
    public List<Integer> listarAtivos(){
        return paymentDayRepository.findByActiveTrueOrderByDayOfMonthAsc().stream()
                .map(PaymentDay::getDayOfMonth)
                .toList();
    }
}
