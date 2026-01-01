package com.viettel.vss.dto.roleGroup;

import lombok.Data;

import java.util.List;

@Data
public class RoleGroupDeleteUserDto {
    private Long roleGroupId;
    private List<Long> userDeleteIdList;
}
