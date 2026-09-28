package com.personaltrainer.billing.plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByActiveTrueOrderByPriceAsc();

    Optional<Plan> findByIdAndActiveTrue(Long id);

}
