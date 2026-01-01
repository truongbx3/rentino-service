package com.viettel.vss.dto;

import lombok.Data;

@Data
public class UserImportDto {
    private String empCode;
    private String fullName;
    private String email;
    private String error;
    private String status;
}
