package com.viettel.vss.dto.function;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.Date;

@Data
public class FunctionImportLogDto extends BaseDto{
    private Long id;
    private String fileName;
    private Boolean isDelete = false;
    private Boolean isActive = true;
    private String createBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}