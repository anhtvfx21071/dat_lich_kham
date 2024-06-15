package com.example.asm3.dto;

import com.example.asm3.entities.Doctor;
import com.example.asm3.entities.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentAdminDto {
    String emailDoctor;
    String title;
    String description;
    LocalDate appointmentDate;
    String registration_time;
    LocalDateTime createAt;
    LocalDateTime deleteAt;
    int status;
    String nameDoctor;
    String nameUser;

    public AppointmentAdminDto() {
    }

    public AppointmentAdminDto(String emailDoctor, String title, String description, LocalDate appointmentDate, String registration_time, LocalDateTime createAt, LocalDateTime deleteAt, int status, String nameDoctor, String nameUser) {
        this.emailDoctor = emailDoctor;
        this.title = title;
        this.description = description;
        this.appointmentDate = appointmentDate;
        this.registration_time = registration_time;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
        this.status = status;
        this.nameDoctor = nameDoctor;
        this.nameUser = nameUser;
    }
}
