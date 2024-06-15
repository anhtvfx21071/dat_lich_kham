package com.example.asm3.dto;

import lombok.Data;

import java.time.LocalDate;
//Thông tin bệnh nhân
@Data
public class PatientInformation {
    private int idAppointment;
    private String name;
    private String emailPatient;
    private String gender;
    private String address;
    private String title;
    private String description;
    private LocalDate appointmentDate;
    private String registration_time;
    private int status;

    public PatientInformation(int idAppointment,String name, String emailPatient, String gender, String address, String title, String description, LocalDate appointmentDate, String registration_time,int status) {
        this.idAppointment =idAppointment;
        this.name = name;
        this.emailPatient = emailPatient;
        this.gender = gender;
        this.address = address;
        this.title = title;
        this.description = description;
        this.appointmentDate = appointmentDate;
        this.registration_time = registration_time;
        this.status = status;
    }
}
