package com.example.asm3.service.impl;

import com.example.asm3.dao.UserRepository;
import com.example.asm3.dto.JwtAuthenticationResponse;
import com.example.asm3.dto.SignInRequest;
import com.example.asm3.dto.SignUpRequest;
import com.example.asm3.entities.Role;
import com.example.asm3.entities.User;
import com.example.asm3.service.AuthenticatinonService;
import com.example.asm3.service.JWTService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticatinonServiceImpl implements AuthenticatinonService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    @Override
    public User signUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setAddress(signUpRequest.getAddress());
        user.setGender(signUpRequest.getGender());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);
        user.setCreate_at(LocalDateTime.now());
        user.setIs_active(1);
        return userRepository.save(user);
    }
    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest){
       Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("email hoặc mk sai"));
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        return jwtAuthenticationResponse;
    }

}
