package com.example.asm3.restControlle;

import com.example.asm3.dto.AppointmentAdminDto;
import com.example.asm3.dto.LockOrUnblockAccount;
import com.example.asm3.dto.SignUpRequest;
import com.example.asm3.dto.UserDto;
import com.example.asm3.entities.*;
import com.example.asm3.service.DoctorCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    @Autowired
    private DoctorCareService doctorCareService;
    //Lấy danh sách bệnh nhân
    @GetMapping("/listUser")
    public List<UserDto> getlistUser(){
        List<UserDto> userDtos = doctorCareService.getListUser();
        return userDtos;
    }
    // Lấy danh sách bác sĩ
    @GetMapping("/listDoctor")
    public List<UserDto> getlistDoctor(){
        List<UserDto> userDtos = doctorCareService.getListDoctor();
        return userDtos;
    }
    //Khóa hoặc hủy khóa tài khoản của bác sĩ và bệnh nhân
    @PostMapping("/lockOrUnlock")
    public String lockOrUnlock(@RequestBody LockOrUnblockAccount lockOrUnblockAccount){
        if(lockOrUnblockAccount.getStatus()==1){
            if(doctorCareService.changeStatusAccount(lockOrUnblockAccount.getEmail(),1)){
                return "Đã mở khóa tài khoản có email " + lockOrUnblockAccount.getEmail();
            }
        }
        if (lockOrUnblockAccount.getStatus() == 0 && lockOrUnblockAccount.getDescription() != null) {
            if(doctorCareService.changeStatusAccount(lockOrUnblockAccount.getEmail(),0)){
                AccountLockInformation lock = new AccountLockInformation(lockOrUnblockAccount.getEmail(), lockOrUnblockAccount.getDescription(), LocalDateTime.now());
                doctorCareService.save(lock);
                return "Đã khóa tài khoản " + lockOrUnblockAccount.getEmail();
            }
        }
        return "Lỗi trong quá trình khóa tài khoản";
    }
    //Thêm bác sĩ
    @PostMapping("/addDoctor")
    public String addDoctor(@RequestBody SignUpRequest signUp){
        try {
            if (signUp.getPassword().equals(signUp.getRepeatPassword())) {
                Department department = doctorCareService.getDepartmentById(signUp.getIdDepartment());
                try {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    User user = new User(signUp.getName(), signUp.getEmail(),passwordEncoder.encode(signUp.getPassword()), signUp.getAddress(), signUp.getPhone(),
                            signUp.getGender(), signUp.getDescription(), Role.DOCTOR, 1, LocalDateTime.now());
                    Doctor doctor = new Doctor(signUp.getSpecialization(),department,user);
                    doctorCareService.save(user);
                    doctorCareService.save(doctor);
                    return "Đăng ký thành công";
                } catch (Exception e) {
                    return "Email đã tồn tại";
                }
            }
        }catch (Exception e){

        }
        return "Đăng ký thất bại có thể do id của khoa không chính xác";
    }
    @GetMapping("/doctorAppointment/{id}")
    public List<AppointmentAdminDto> doctorAppointment(@PathVariable int id){
        return doctorCareService.getListAppointmentByIdDoctor(id);
    }
    @GetMapping("/userAppointment/{id}")
    public List<AppointmentAdminDto> userAppointment(@PathVariable int id){
        return doctorCareService.getListAppointmentByIdUser(id);
    }
}
