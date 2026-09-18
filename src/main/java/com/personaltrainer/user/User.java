package com.personaltrainer.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Table
@Entity (name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String user;

    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime lastLoginAt;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column (nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null){
            this.status = UserStatus.ACTIVE;
        }

    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
