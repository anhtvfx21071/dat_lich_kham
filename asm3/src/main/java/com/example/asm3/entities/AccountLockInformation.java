package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "account_lock_information")
public class AccountLockInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String email;
    String description;
    LocalDateTime create_at;

    public AccountLockInformation(String email, String description, LocalDateTime create_at) {
        this.email = email;
        this.description = description;
        this.create_at = create_at;
    }

    public AccountLockInformation() {

    }
}
