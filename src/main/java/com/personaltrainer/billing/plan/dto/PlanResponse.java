package com.personaltrainer.billing.plan.dto;

import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.billing.plan.PlanPeriodicity;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
       String name,
       BigDecimal price,
       PlanPeriodicity periodicity
) {
    public static PlanResponse from (Plan plan){
        return new PlanResponse(plan.getId(), plan.getName(), plan.getPrice(), plan.getPeriodicity());
    }
}
