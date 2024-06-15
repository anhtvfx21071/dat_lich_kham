package com.example.asm3.service;

import com.example.asm3.dto.JwtAuthenticationResponse;
import com.example.asm3.dto.SignInRequest;
import com.example.asm3.dto.SignUpRequest;
import com.example.asm3.entities.Role;
import com.example.asm3.entities.User;

public interface AuthenticatinonService{
    User signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
}
