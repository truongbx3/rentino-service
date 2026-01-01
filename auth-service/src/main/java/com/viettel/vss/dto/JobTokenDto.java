package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;


@Data
public class JobTokenDto extends BaseDto {

    private String username;

    private String password;
}
