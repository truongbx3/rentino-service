package com.viettel.vss.dto.roleGroup;

import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.user.UserResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class RoleGroupInfoResponseDto {
    private RoleGroupDto roleGroupDto;
    private List<UserResponseDto> lsUser;
    private List<FunctionResponseDto> lsFunction;
}
