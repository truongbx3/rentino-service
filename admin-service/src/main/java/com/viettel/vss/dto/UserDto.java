package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends BaseDto{
    private Long id;
    private String username;
    private String empCode;
    private String password;
    private String fullName;
    private String email;
    private String company;
    private String companyCode;
    private Integer failedAttempts = 0;
    private Boolean accountLocked = false;
    private Date firstFailedLoginTime;
    private Date lastFailedLoginTime;
    private String salt;
    private Boolean isFirstLogin;
    private Long statusId;
    private String functionCode;
    private String description;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
}