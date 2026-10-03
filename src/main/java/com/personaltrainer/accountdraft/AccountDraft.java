package com.personaltrainer.accountdraft;

import com.personaltrainer.billing.payment.PixKey;
import com.personaltrainer.billing.plan.Plan;
import com.personaltrainer.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "account_drafts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AccountDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "plan_id")
    private Plan plan;

    private Integer paymentDay;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "pix_key_id")
    private PixKey pixKey;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    @Builder.Default
    private AccountDraftStatus status = AccountDraftStatus.PENDING_PAYMENT;

    private LocalDateTime paymentConfirmedAt;

    private LocalDateTime reviewedAt;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "reviewer_id")
    private User reviewer;

    @Column (columnDefinition = "TEXT")
    private String rejectionReason;

    @Column (nullable = false)
    private LocalDateTime expiresAt;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate (){
        this.createdAt = LocalDateTime.now();
        if (this.status == null){
            this.status = AccountDraftStatus.PENDING_PAYMENT;
        }
    }
}
