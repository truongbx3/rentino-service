package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

@Data
public class TokenKcResponse extends BaseDto {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private Long refresh_expires_in;
    private String scope;
}
