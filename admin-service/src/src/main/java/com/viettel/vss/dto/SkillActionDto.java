package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class SkillActionDto extends BaseDto {
    private Long skillId;
    private Long actionId;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}
