package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsersDto extends BaseDto{

    @NotNull
    private Long id;
    

    private String username;

    private String name;

    private String email;
    

    private String phone;
    

    private Integer userType;
    

    private String address;
    

    private String company;
    

    private String companyCode;
    

    private Integer isAdmin;
    
}