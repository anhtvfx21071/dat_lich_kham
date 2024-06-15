package com.example.asm3.restControlle;

import com.example.asm3.dto.DoctorDto;
import com.example.asm3.entities.*;
import com.example.asm3.service.DoctorCareService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    private DoctorCareService doctorCareService;

    //Danh sách khoa nổi bật  có lịch đặt nhiều
    @GetMapping("/outstandingMedicalDepartment")
    public List<Department> outstandingMedicalDepartment() {
        List<Department> departments = doctorCareService.outstandingMedicalDepartment();
        return departments;
    }

    //Danh sách bệnh viện nổi bật có nhiều khoa
    @GetMapping("/outstandingHospital")
    public List<Hospital> outstandingHospital() {
        List<Hospital> hospitals = doctorCareService.outstandingHospital();
        return hospitals;
    }
    //Thông tin cơ bản của bệnh nhân
    @GetMapping("/userInformation")
    public User userInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User user1 = doctorCareService.getUserByEmail(user.getEmail());
        user1.setAppointments(null);
        return user1;
    }
    //cập nhật thông tin bệnh nhân
    @PostMapping(value = "/update",consumes = { "multipart/form-data" })
    public String updateUser(HttpServletRequest request) {
        MultipartFile avatar = ((MultipartHttpServletRequest) request).getFile("file");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        try {
            File logoCurrent = new File("C:\\image\\avatar\\" + user.getAvatar());
            if (logoCurrent.exists()) {
                logoCurrent.delete();
            }
        } catch (Exception e) {
        }
        try {
            File file = new File("C:\\image\\avatar\\");
            if (!file.exists()) {
                file.mkdirs();
            }
            File logoNew = new File("C:\\image\\avatar\\" +"-"+user.getId()+"-"+avatar.getOriginalFilename());
            try {
                avatar.transferTo(logoNew);
            } catch (Exception e) {
            }
            user.setName(request.getParameter("name"));
            user.setAvatar("-"+user.getId()+"-"+avatar.getOriginalFilename());
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            BCryptPasswordEncoder pass = new BCryptPasswordEncoder();
            user.setPassword(pass.encode(request.getParameter("password")));
            user.setGender(request.getParameter("gender"));
            user.setDescription(request.getParameter("description"));
            user.setAddress(request.getParameter("address"));
            user.setUpdate_at(LocalDateTime.now());
            doctorCareService.update(user);
            return "Cập nhật thành công";
        }catch (Exception e){
            return "Cập nhật thông tin thất bại";
        }
    }
    //Tìm kiếm chung
    @RequestMapping("/generalSearch/{key}")
    public List<DoctorDto> generalSearch(@PathVariable(value = "key") String key) {
        List<DoctorDto> doctors = doctorCareService.generalSearch(key);
        return doctors;
    }

    //tìm kiếm theo chuyên khoa
    @RequestMapping("/searchByDepartment/{key}")
    public List<DoctorDto> searchByDepartment(@PathVariable(value = "key") String key) {
        List<DoctorDto> doctors = doctorCareService.searchByDepartment(key);
        return doctors;
    }

    // Đặt lịch khám bênh
    @PostMapping("/bookMedicalExamination")
    public String bookMedicalExamination(@RequestBody Appointment appointment) {
        if (appointment.getEmailDoctor() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            appointment.setUser(doctorCareService.getUserByEmail(user.getEmail()));
            appointment.setDoctor(doctorCareService.getDoctorByEmail(appointment.getEmailDoctor()));
            appointment.setCreateAt(LocalDateTime.now());
            doctorCareService.bookMedicalExamination(appointment);
            return "đặt lịch thành công";
        }
        return "đặt lịch thất bại";
    }
}
