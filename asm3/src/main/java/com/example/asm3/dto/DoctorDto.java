package com.example.asm3.dto;

import lombok.Data;
//Thông tin bác sĩ
@Data
public class DoctorDto {
    private String doctorName;
    private String addressHospital;
    private String departmentName;
    private String hospitalName;
    private String specialization;
    private String doctorEmail;
    private String doctorPhone;

    public DoctorDto(String doctorName, String addressHospital, String departmentName, String hospitalName, String specialization, String doctorEmail, String doctorPhone) {
        this.doctorName = doctorName;
        this.addressHospital = addressHospital;
        this.departmentName = departmentName;
        this.hospitalName = hospitalName;
        this.specialization = specialization;
        this.doctorEmail = doctorEmail;
        this.doctorPhone = doctorPhone;
    }
}
