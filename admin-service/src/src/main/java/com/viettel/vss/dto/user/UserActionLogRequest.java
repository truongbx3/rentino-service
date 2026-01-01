package com.viettel.vss.dto.user;

import com.viettel.vss.dto.roleGroup.RoleGroupResponseDto;
import jdk.jfr.Description;
import lombok.Data;

import java.util.List;

@Data
public class UserActionLogRequest {
    List<UserInfoActionDto> users;

    @Description("Chỉ bao gồm 2 giá trị LOCK / UNLOCK")
    private String action; // LOCK / UNLOCK
}
