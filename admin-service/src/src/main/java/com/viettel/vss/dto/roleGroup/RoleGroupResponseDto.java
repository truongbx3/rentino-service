package com.viettel.vss.dto.roleGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroupResponseDto {
    private Long id;
    private String roleGroupName;
    private String roleGroupCode;
    private String description;
    private Long numberUser;

    public  RoleGroupResponseDto(Long id, String roleGroupName, String roleGroupCode, String description) {
        this.id = id;
        this.roleGroupName = roleGroupName;
        this.roleGroupCode = roleGroupCode;
        this.description = description;
    }
}
