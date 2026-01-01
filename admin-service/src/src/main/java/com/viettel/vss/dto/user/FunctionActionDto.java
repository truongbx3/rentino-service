package com.viettel.vss.dto.user;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class FunctionActionDto extends BaseDto {
    private Long functionId;
    private Long actionId;
    private Integer orderIndex;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}
