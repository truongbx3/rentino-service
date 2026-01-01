package com.viettel.vss.dto.response;

import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private UserDto user;
    private String accessToken;
    private Long expiresIn;
}
