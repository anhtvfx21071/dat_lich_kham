package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String specialization;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "department_id")
    Department department;
    @OneToOne
    @JoinColumn(name = "user_id")
    User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    List<Appointment> appointments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctorEx")
    List<Extrainfos> extrainfos;

    public Doctor(String specialization, Department department, User user) {
        this.specialization = specialization;
        this.department = department;
        this.user = user;
    }

    public Doctor() {

    }
}
