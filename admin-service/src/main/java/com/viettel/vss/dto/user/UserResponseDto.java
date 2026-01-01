package com.viettel.vss.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viettel.vss.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto extends UserDto {
    private Long id;

    private String username;

    private String fullName;

    private String company;

    private String companyCode;

    private String email;

    private String objectType;

    private String objectName;

    private String contract;

    private String contractName;

    private String roles;

    private Date createdDate;

    private Date updatedDate;

    private String createdBy;

    private String updatedBy;
}

