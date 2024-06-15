package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "extrainfos")
public class Extrainfos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String description;
    int status;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "doctor_id")
    Doctor doctorEx;
    @OneToOne
    @JoinColumn(name = "appointment_id")
    Appointment appointment;

    public Extrainfos(String description, int status, Doctor doctorEx, Appointment appointment) {
        this.description = description;
        this.status = status;
        this.doctorEx = doctorEx;
        this.appointment = appointment;
    }

    public Extrainfos() {

    }
}

