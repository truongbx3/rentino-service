package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;


@Data
public class JobTokenResponse extends BaseDto {
    private String accessToken;
    private Long expiresIn;
}
