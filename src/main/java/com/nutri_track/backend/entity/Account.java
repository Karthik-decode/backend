
package com.nutri_track.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "accounts",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_account_email",
        columnNames = "email"
    )
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Account() {
    }

    public Account(String name, String email, String pinHash) {
        this.name = name;
        this.email = email;
        this.pinHash = pinHash;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPinHash() {
        return pinHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
