package com.viettel.vss.dto.response;

import com.viettel.vss.dto.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAppResponse {
    private UsersDto user;
    private String toolCode;
    private String versionCode;
    private String companyCode;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private Date serviceStart;
    private Date serviceExpired;
}
