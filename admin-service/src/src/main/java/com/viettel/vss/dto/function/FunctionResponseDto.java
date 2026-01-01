package com.viettel.vss.dto.function;

import com.viettel.vss.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionResponseDto extends BaseDto{
    private Long id;
    private String functionCode;
    private String description;
    private Boolean isActive;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String menuName;
    private Long menuId;
    private Long roleGroupId;

    public FunctionResponseDto(Long id, String functionCode, String description, String createdBy, Date createdDate, String updatedBy, Date updatedDate, Long roleGroupId) {
        this.id = id;
        this.functionCode = functionCode;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.roleGroupId = roleGroupId;
    }

    public FunctionResponseDto(Long id, String functionCode, String description, Integer isActive, String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
        this.id = id;
        this.functionCode = functionCode;
        this.description = description;
        this.isActive = isActive == 1;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }
}