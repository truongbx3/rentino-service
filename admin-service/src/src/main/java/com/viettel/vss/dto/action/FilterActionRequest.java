package com.viettel.vss.dto.action;

import com.viettel.vss.dto.support.DateSupport;
import lombok.Data;

import java.util.List;

@Data
public class FilterActionRequest {
    private String key;
    private String name;
    private List<Long> skillIds;
    private DateSupport createdDate;
    private DateSupport updatedDate;
}
