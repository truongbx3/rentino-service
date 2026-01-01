package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class ActionDto extends BaseDto {
    private Long id;
    private String key;
    private String name;
    private String description;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}
