package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

@Data
public class ChangePwDto extends BaseDto {
    private String phone;
    private String otp;
    private String newPassword;
    private String rePassword;
}
