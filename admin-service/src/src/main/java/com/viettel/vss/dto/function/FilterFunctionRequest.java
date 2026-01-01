package com.viettel.vss.dto.function;

import com.viettel.vss.dto.support.DateSupport;
import lombok.Data;

@Data
public class FilterFunctionRequest {
    private String functionCode;
    private String description;
    private Integer isDeleted = 0;
    private Integer isActive;
    private String createdBy;
    private DateSupport createdDate;
    private String updatedBy;
    private DateSupport updatedDate;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdDate";
    private String sortDirection = "DESC";
}
