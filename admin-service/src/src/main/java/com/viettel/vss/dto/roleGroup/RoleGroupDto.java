package com.viettel.vss.dto.roleGroup;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RoleGroupDto extends BaseDto{
    private Long id;
    @NotBlank(message = "Tên nhóm quyền không được để trống")
    private String roleGroupName;
    private String description;
    private Boolean isActive = true;
    private Boolean defaultGroup = false;
    private Boolean isAdmin = false;
    private List<Long> userIds;
    private List<Long> functionIds;
}