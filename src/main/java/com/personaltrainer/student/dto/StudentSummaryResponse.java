package com.personaltrainer.student.dto;

import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.billing.plan.dto.PlanResponse;
import com.personaltrainer.student.Student;

import java.time.LocalDateTime;

public record StudentSummaryResponse(
        Long id,
        String name,
        String email,
        String phone,
        PlanResponse plan,
        Integer paymentDay,
        Boolean active,
        Boolean profileCompleted,
        LocalDateTime approvedAt
) {
    public static StudentSummaryResponse from(Student s, Plan plan) {
        return new StudentSummaryResponse(
                s.getId(),
                s.getUser().getName(),
                s.getUser().getEmail(),
                s.getPhone(),
                plan != null ? PlanResponse.from(plan) : null,
                s.getPaymentDay(),
                s.getActive(),
                s.getProfileCompleted(),
                s.getCreatedAt()
        );
    }
}
