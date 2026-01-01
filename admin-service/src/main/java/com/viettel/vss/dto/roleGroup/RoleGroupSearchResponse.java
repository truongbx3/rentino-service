package com.viettel.vss.dto.roleGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroupSearchResponse {
    private Long id;
    private String roleGroupName;
    private String roleGroupCode;
    private String description;
}
