package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String emailDoctor;
    String title;
    String description;
    LocalDate appointmentDate;
    String registration_time;
    LocalDateTime createAt;
    LocalDateTime deleteAt;
    int status;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "doctor_id")
    Doctor doctor;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id")
    User user;
}
