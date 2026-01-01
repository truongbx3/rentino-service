package com.viettel.vss.dto.executableFunc;

import com.viettel.vss.dto.support.DateSupport;
import lombok.Data;

import java.util.List;

@Data
public class FilterExecutableFuncRequest {
    private String keyCode;
    private String name;
    private List<Long> executionTypeId;
    private List<Long> systemId;
    private Integer isDeleted = 0;
    private Integer isActive;
    private DateSupport createdDate;
    private DateSupport updatedDate;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdDate";
    private String sortDirection = "DESC";
}
