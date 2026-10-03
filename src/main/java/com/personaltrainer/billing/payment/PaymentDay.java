package com.personaltrainer.billing.payment;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "payment_days")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDay {

    @Id
    @Column (name = "day_of_month")
    private Integer dayOfMonth;

    @Column (nullable = false)
    @Builder.Default
    private Boolean active = true;
}
