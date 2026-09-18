package com.personaltrainer.user;

import com.personaltrainer.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
@Entity (name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Student {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false, unique = true)
    private User user;

    private String phone;

    private LocalDate birthDate;

    @Column (nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column (nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
