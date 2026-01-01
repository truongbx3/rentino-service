package com.viettel.vss.dto.system;

import com.viettel.vss.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SystemResponseDto extends BaseDto{
    private Long id;
    private String name;
    private String baseUrl;
    private String description;
    private Integer isActive = 1;
    private Integer isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String credential;
    private String authType;
    private String authTypeName;
    private Long fetchDuration;

    public SystemResponseDto(Long id, String name, String baseUrl, String description, Integer isActive, Integer isDeleted,
                             String createdBy, Date createdDate, String updatedBy, Date updatedDate, String credential, String authType, Long fetchDuration) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.credential = credential;
        this.authType = authType;
        this.fetchDuration = fetchDuration;
    }
}