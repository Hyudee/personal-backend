package com.personaltrainer.billing.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDayRepository extends JpaRepository<PaymentDay, Integer> {

    List<PaymentDay> findByActiveTrueOrderByDayOfMonthAsc();

    boolean existsByDayOfMonthAndActiveTrue (Integer dayOfMonth);
}
