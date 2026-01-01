package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.*;


@Data
public class LoginDto extends BaseDto {

    private String phone;

    private String password;

}
