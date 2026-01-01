package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class AuthTypeDto extends BaseDto{
    private Long id;
    private String code;
    private String name;
    private String description;
    private int isActive = 1;
    private int isDelete = 0;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}