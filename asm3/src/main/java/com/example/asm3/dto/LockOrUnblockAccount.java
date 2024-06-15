package com.example.asm3.dto;

import lombok.Data;
//Khóa và hủy khóa tài khoản
@Data
public class LockOrUnblockAccount {
    private String email;
    private int status;
    private String description;
}
