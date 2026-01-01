package com.viettel.vss.dto.user;

import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.roleGroup.RoleGroupResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DetailInfoStaff {
    private UserResponseDto userDTO;
    private List<RoleGroupResponseDto> lsRoles;
    private List<FunctionResponseDto> lsFunctions;
}
