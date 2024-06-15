package com.example.asm3.restControlle;

import com.example.asm3.dao.UserRepository;
import com.example.asm3.dto.JwtAuthenticationResponse;
import com.example.asm3.dto.ResetPassword;
import com.example.asm3.dto.SignInRequest;
import com.example.asm3.dto.SignUpRequest;
import com.example.asm3.entities.Role;
import com.example.asm3.entities.User;
import com.example.asm3.service.AuthenticatinonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicRestController {
    private final AuthenticatinonService authenticatinonService;
    @Autowired
    private final UserRepository userRepository;
    //Đăng ký tài khoản đối với bệnh nhân
    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getPassword().equals(signUpRequest.getRepeatPassword())) {
            try {
                ResponseEntity.ok(authenticatinonService.signUp(signUpRequest));
                return "Đăng ký thành công";
            } catch (Exception e) {
                return "Email đã tồn tại";
            }
        }
        return "Đăng ký thất bại";
    }
    //Đăng nhập tài khoản
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            JwtAuthenticationResponse jwt = authenticatinonService.signIn(signInRequest);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đăng nhập bị lỗi");
        }
    }


    //Quên mật khẩu gửi mail mã xác thực
    @GetMapping("/forgotPassword/{email}")
    public String forgotPassword(@PathVariable(value = "email") String email, HttpServletRequest request) {
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
        int min = 100000;
        int max = 999999;
        String code = String.valueOf((int) (Math.random() * (max - min + 1) + min));
        Map<String,String> data = new HashMap<>();
        data.put(email, code);
        HttpSession saveCode = request.getSession();
        saveCode.setAttribute("verifyEmail", data);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Ma xac thuc ");
            message.setText("Ma xac thuc cua ban la: " + code);
            Transport.send(message);
        } catch (Exception e) {
            return "Không gửi được email xác thực";
        }
        return "http://localhost:8080/public/verity";
    }

    //Kiểm tra mã gửi về email và mã nhập vào hợp lệ không và tiến hành đổi mật khẩu
    @PostMapping("/verity")
    public String verityEmail(@RequestBody ResetPassword resetPassword, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, String> value = (Map<String, String>) session.getAttribute("verifyEmail");

        if (value.get(resetPassword.getEmail()).equals(resetPassword.getCode()) && resetPassword.getPassword().equals(resetPassword.getRepeatPassword())) {
            try {
                Optional<User> user = userRepository.findByEmail(resetPassword.getEmail());
                User user1 = user.get();
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user1.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
                userRepository.save(user1);
                return "Thay đổi mật khẩu thành công";
            } catch (Exception e) {
                return "Email sai";
            }
        }
        return "Thay đổi mật khẩu không thành công";
    }

}
