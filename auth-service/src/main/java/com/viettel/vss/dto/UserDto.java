package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends BaseDto{
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String salt;
    private String phone;
    private Boolean isDeleted = false;
    private Boolean isFirstLogin = false;
}