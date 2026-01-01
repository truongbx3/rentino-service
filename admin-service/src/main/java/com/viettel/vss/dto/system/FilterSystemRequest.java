package com.viettel.vss.dto.system;

import com.viettel.vss.dto.support.DateSupport;
import lombok.Data;

@Data
public class FilterSystemRequest {
    private String systemName;
    private String baseUrl;
    private String description;
    private Integer isDeleted = 0;
    private Integer isActive;
    private DateSupport createdDate;
    private DateSupport updatedDate;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdDate";
    private String sortDirection = "DESC";
}
