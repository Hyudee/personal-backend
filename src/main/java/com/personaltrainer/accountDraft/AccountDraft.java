package com.personaltrainer.accountDraft;

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
    private long id;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

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
