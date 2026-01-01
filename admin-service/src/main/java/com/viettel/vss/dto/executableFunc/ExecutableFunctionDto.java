package com.viettel.vss.dto.executableFunc;

import com.viettel.vss.base.BaseDto;
import com.viettel.vss.entity.ExecutionType;
import com.viettel.vss.entity.System;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutableFunctionDto extends BaseDto{
    private Long id;
    @NotBlank(message = "Key hàm thực thi không được để trống")
    private String keyCode;
    @NotBlank(message = "Tên hàm thực thi không được để trống")
    private String name;
    private String dataInputType;
    private String metaData;
    private int isActive = 1;
    private int isDeleted = 0;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    private Long systemId;
    @NotBlank(message = "Loại hàm thực thi không được để trống")
    private Long executionTypeId;

    private System system;
    private ExecutionType executionType;
}