package com.viettel.vss.dto.function;

import java.util.Date;

public interface FunctionResponseProjection {
    Long getId();
    String getFunctionCode();
    String getDescription();
    Boolean getIsActive();
    String getCreatedBy();
    Date getCreatedDate();
    String getUpdatedBy();
    Date getUpdatedDate();
    String getMenuName();
    Long getMenuId();
    Long getRoleGroupId();
}
