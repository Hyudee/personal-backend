package com.personaltrainer.billing.plan;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table (name = "plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Plan {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanPeriodicity periodicity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

}
