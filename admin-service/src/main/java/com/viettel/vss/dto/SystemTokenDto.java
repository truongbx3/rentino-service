package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class SystemTokenDto extends BaseDto{
    private Long id;
    private String token;
    private Date expriedAt;
    private Integer isActive = 1;
    private Integer isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}