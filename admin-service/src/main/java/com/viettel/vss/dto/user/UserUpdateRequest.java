package com.viettel.vss.dto.user;

import com.viettel.vss.dto.roleGroup.RoleGroupResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {
    private UserCrudRequest userDTO;
    private List<RoleGroupResponseDto> lsRoles;
}
