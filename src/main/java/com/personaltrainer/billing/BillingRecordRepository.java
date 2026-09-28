package com.personaltrainer.billing;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRecordRepository extends JpaRepository <BillingRecord, Long> {
}
