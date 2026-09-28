package com.personaltrainer.billing.payment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "pix_keys")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PixKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "key_value", nullable = false, unique = true)
    private String keyValue;

    @Column (nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
