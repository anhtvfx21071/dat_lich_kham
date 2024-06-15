package com.example.asm3.service.impl;

import com.example.asm3.dao.DoctorCareDao;
import com.example.asm3.dto.AppointmentAdminDto;
import com.example.asm3.dto.DoctorDto;
import com.example.asm3.dto.PatientInformation;
import com.example.asm3.dto.UserDto;
import com.example.asm3.entities.*;
import com.example.asm3.service.DoctorCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoctorCareServiceImpl implements DoctorCareService {
    @Autowired
    private DoctorCareDao doctorCareDao;
    //Thông tin khoa nổi bật
    @Override
    @Transactional
    public List<Department> outstandingMedicalDepartment() {
        return doctorCareDao.outstandingMedicalDepartment();
    }
    //Bệnh viện nổi bật
    @Override
    @Transactional
    public List<Hospital> outstandingHospital() {
        return doctorCareDao.outstandingHospital();
    }
    //Tìm kiếm bác sĩ cục bộ
    @Override
    @Transactional
    public List<DoctorDto> generalSearch(String key) {
        return doctorCareDao.generalSearch(key);
    }
    //Tìm kiếm bác sĩ theo khoa
    @Override
    @Transactional
    public List<DoctorDto> searchByDepartment(String key) {
        return doctorCareDao.searchByDepartment(key);
    }
    //Đặt lịch khám
    @Override
    @Transactional
    public void bookMedicalExamination(Appointment appointment) {
        doctorCareDao.bookMedicalExamination(appointment);
    }
    //Lấy đối tượng bác sĩ thông qua email
    @Override
    @Transactional
    public Doctor getDoctorByEmail(String email) {
        return doctorCareDao.getDoctorByEmail(email);
    }
    //Lấy đối tượng bệnh nhân thông qua email
    @Override
    @Transactional
    public User getUserByEmail(String email) {
        return doctorCareDao.getUserByEmail(email);
    }
    //Lấy các lịch đặt thông qua email
    @Override
    @Transactional
    public List<PatientInformation> getAppointmentByEmail(String email) {
        return doctorCareDao.getAppointmentByEmail(email);
    }
    // thay đổi trạng thái lịch đặt
    @Override
    @Transactional
    public void changeStatusAppointment(int idAppointment, int status) {
        doctorCareDao.changeStatusAppointment(idAppointment, status);
    }
    //Lưu thông tin bổ sung khi hủy cuộc họp
    @Override
    @Transactional
    public void saveExtrainfos(int idAppointment, String description) {
        doctorCareDao.saveExtrainfos(idAppointment,description);
    }
    // Kiểm tra xem bác sĩ có phải là người phụ trách của lịch khám bệnh đó không
    @Override
    @Transactional
    public boolean checkAppointmentId(String email, int idAppointment) {
        return doctorCareDao.checkAppointmentId(email, idAppointment);
    }
    //Lấy danh sách bác sĩ
    @Override
    @Transactional
    public List<UserDto> getListDoctor() {
        return doctorCareDao.getListDoctor();
    }
    //Lấy danh sách bệnh nhân
    @Override
    @Transactional
    public List<UserDto> getListUser() {
        return doctorCareDao.getListUser();
    }
    //Thay đổi trạng thái tài khoản
    @Override
    @Transactional
    public boolean changeStatusAccount(String email, int status) {
        return doctorCareDao.changeStatusAccount(email,status);
    }
    //Lấy khoa khám bệnh thông qua id
    @Override
    @Transactional
    public Department getDepartmentById(int id) {
        return doctorCareDao.getDepartmentById(id);
    }
    //Lưu đối tượng
    @Override
    @Transactional
    public <T> void save(T t) {
        doctorCareDao.save(t);
    }
    // lay danh sach kham cua bac si
    @Override
    @Transactional
    public List<AppointmentAdminDto> getListAppointmentByIdDoctor(int id) {
        return doctorCareDao.getListAppointmentByIdDoctor(id);
    }
    //Lay danh sach kham cua benh nha
    @Override
    @Transactional
    public List<AppointmentAdminDto> getListAppointmentByIdUser(int id) {
        return doctorCareDao.getListAppointmentByIdUser(id);
    }

    @Override
    @Transactional
    public <T> void update(T t) {
        doctorCareDao.update(t);
    }
}
