package com.example.asm3.service;

import com.example.asm3.dto.AppointmentAdminDto;
import com.example.asm3.dto.DoctorDto;
import com.example.asm3.dto.PatientInformation;
import com.example.asm3.dto.UserDto;
import com.example.asm3.entities.*;

import java.util.List;

public interface DoctorCareService {
    List<Department> outstandingMedicalDepartment();
    List<Hospital> outstandingHospital();
    List<DoctorDto> generalSearch(String key);
    List<DoctorDto> searchByDepartment(String key);
    void bookMedicalExamination(Appointment appointment);
    Doctor getDoctorByEmail(String email);
    User getUserByEmail(String email);
    List<PatientInformation> getAppointmentByEmail(String email);
    void changeStatusAppointment(int idAppointment, int status);
    void saveExtrainfos(int idAppointment, String décription);
    boolean checkAppointmentId(String email, int idAppointment);
    List<UserDto> getListDoctor();
    List<UserDto> getListUser();
    boolean changeStatusAccount(String email, int status);

    Department getDepartmentById(int id);
    <T> void save(T t);
    List<AppointmentAdminDto> getListAppointmentByIdDoctor(int id);
    List<AppointmentAdminDto> getListAppointmentByIdUser(int id);
    <T> void update(T t);
}
