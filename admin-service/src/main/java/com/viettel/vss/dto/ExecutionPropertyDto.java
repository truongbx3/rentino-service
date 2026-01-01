package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class ExecutionPropertyDto extends BaseDto{
    private Long id;
    private Long parentPropertyId;
    private String propertyName;
    private String dataType;
    private String description;
    private int required = 1;
    private String defaultValue;
    private Integer indexOrder;
    private int isActive = 1;
    private int isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    private Long executionTypeId;
//    private ExecutionTypeEntity executionType;
}