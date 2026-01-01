package com.viettel.vss.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenVerify {
    private long id;
    private String phone;
    private String email;
    private String token;
}
