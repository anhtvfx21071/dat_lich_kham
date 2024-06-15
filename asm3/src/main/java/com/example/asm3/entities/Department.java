package com.example.asm3.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Data
@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String phone;
    String description;
    Integer status;
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "hospital_id")
    Hospital hospital;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    Set<Doctor> doctors;
}
