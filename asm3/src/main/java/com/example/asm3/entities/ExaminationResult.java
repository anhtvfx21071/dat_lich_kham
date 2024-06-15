package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "examination_result")
public class ExaminationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String fileName;
    String emailDoctor;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id")
    User userExa;
    LocalDateTime time;

    public ExaminationResult(String fileName, String emailDoctor, User userExa, LocalDateTime time) {
        this.fileName = fileName;
        this.emailDoctor = emailDoctor;
        this.userExa = userExa;
        this.time = time;
    }

    public ExaminationResult() {

    }
}
