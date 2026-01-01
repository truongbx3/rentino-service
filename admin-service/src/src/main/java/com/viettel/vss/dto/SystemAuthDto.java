package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SystemAuthDto extends BaseDto{
    private Long id;
    private String authType;
    private String credential;
    private Date lastFetchAt;
    private Date nextFetchAt;
    private Long fetchDuration;
    private Integer isActive = 1;
    private Integer isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private List<SystemTokenDto> systemTokens;
}