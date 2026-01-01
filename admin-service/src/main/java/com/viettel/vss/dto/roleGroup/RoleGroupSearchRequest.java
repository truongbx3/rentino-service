package com.viettel.vss.dto.roleGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroupSearchRequest {
    private String roleGroupName;
    private int isActive = 1;
    private Long userId;
    private int isDeleted = 0;
    private Integer page;
    private Integer size;
}