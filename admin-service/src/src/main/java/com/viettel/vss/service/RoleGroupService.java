package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.roleGroup.*;
import com.viettel.vss.entity.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleGroupService extends BaseService<RoleGroup, RoleGroupDto>{

    List<RoleGroupResponseDto> listRoleGroup();

    void deleteRoleGroup(Long id);

    Long saveRoleGroup(RoleGroupDto roleGroupDto);

    Long updateRoleGroup(RoleGroupDto roleGroupDto);

    RoleGroupInfoResponseDto roleGroupInfo(Long roleGroupId);

    List<RoleGroupDto> findAllRoleGroupDefault();

    boolean existAdminRoleGroup();

    Page<RoleGroupSearchResponse> searchRoleGroup(RoleGroupSearchRequest roleGroupSearchRequest, Pageable pageable);

    Boolean deleteUserInGroup(RoleGroupDeleteUserDto roleGroupDto);

    boolean addStaffsToRoleGroupDefault(List<UserDto> userDtos);

    Boolean checkAndDeleteUserFromDefaultGroup(List<Long> staffIds);
}