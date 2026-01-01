package com.viettel.vss.dto.function;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class FunctionDto extends BaseDto{
    private Long id;
    private String name;
    private String functionCode;
    private String description;
    private Boolean isActive = true;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String menuName;
    private Long menuId;
    private Long roleGroupId;

    private String error;
    private String inputActiveStatus;
}