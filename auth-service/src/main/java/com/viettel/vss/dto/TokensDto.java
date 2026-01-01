package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

@Data
public class TokensDto extends BaseDto {
    private String accessToken;
    private String email;
    private String companyCode;
    private String typeApp;
}
