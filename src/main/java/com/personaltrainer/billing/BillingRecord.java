package com.personaltrainer.billing;

import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table (name = "pix_keys")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BillingRecord {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "student_id", nullable = false)
    private Student student;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "plan_id", nullable = false)
    private Plan plan;

    @Column (nullable = false)
    private LocalDate dueDate;

    private LocalDateTime paidAt;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    @Builder.Default
    private BillingStatus status = BillingStatus.PENDING;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}