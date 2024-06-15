package com.example.asm3.restControlle;

import com.example.asm3.dto.AppointmentDto;
import com.example.asm3.dto.PatientInformation;
import com.example.asm3.entities.ExaminationResult;
import com.example.asm3.entities.User;
import com.example.asm3.service.DoctorCareService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/doctor")
public class DoctorRestController {
    @Autowired
    private DoctorCareService doctorCareService;

    //danh sách bệnh nhân đăng ký khám
    @GetMapping("/patientList")
    public List<PatientInformation> patientList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<PatientInformation> patients = doctorCareService.getAppointmentByEmail(user.getEmail());
        return patients;
    }

    //nhận hoặc không nhận lịch khám
    @PostMapping("/acceptOrCancel")
    public String acceptOrCancel(@RequestBody AppointmentDto appointmentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        //Kiểm tra id cuộc hẹn có phải của bác sĩ không
        if (doctorCareService.checkAppointmentId(user.getEmail(), appointmentDto.getAppointmentId())) {
            if (appointmentDto.getStatus() == 1) {
                doctorCareService.changeStatusAppointment(appointmentDto.getAppointmentId(), 1);
                return "Đã nhận lịch đăng ký khám có id " + appointmentDto.getAppointmentId() + " thành công";
            } else if (appointmentDto.getStatus() == 2 && appointmentDto.getDescription() != null) {
                doctorCareService.changeStatusAppointment(appointmentDto.getAppointmentId(), 2);
                doctorCareService.saveExtrainfos(appointmentDto.getAppointmentId(), appointmentDto.getDescription());
                return "Hủy lịch đăng ký khám có id " + appointmentDto.getAppointmentId() + " thành công";
            }
            return "Lỗi status khi thực hiện";
        }
        return "Id của lịch đặt không nằm trong quản lý của bác sĩ";
    }

    //Gửi kết quả khám cho bệnh nhân
    @PostMapping("/examinationResult")
    public String result(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User user1 = doctorCareService.getUserByEmail(request.getParameter("email"));
        if (user1 == null) {
            return "Email của bệnh nhân vị lỗi";
        }
        String senderEmail = "tranvananh332000@gmail.com";
        String senderPassword = "nzvsmidzpecoyzld";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.getParameter("email")));
            message.setSubject("Kết quả khám Bệnh");
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            BodyPart attachmentBodyPart = new MimeBodyPart();
            InputStream fileInputStream = file.getInputStream();
            attachmentBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, file.getContentType())));
            attachmentBodyPart.setFileName(file.getOriginalFilename());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            File file1 = new File("C:\\image\\result\\");
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File fileResult = new File("C:\\image\\result\\" + file.getOriginalFilename());
            file.transferTo(fileResult);
            ExaminationResult examinationResult = new ExaminationResult(file.getOriginalFilename(), user.getEmail(), user1, LocalDateTime.now());
            doctorCareService.save(examinationResult);
        } catch (Exception e) {
            return "Không gửi được";
        }
        return "Đã gửi kết quả khám bệnh cho bệnh nhân";
    }
}
