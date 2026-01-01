package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.roleGroup.*;
import com.viettel.vss.dto.user.SearchUserDto;
import com.viettel.vss.dto.user.UserResponseDto;
import com.viettel.vss.entity.*;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.*;
import com.viettel.vss.service.RoleGroupService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RoleGroupServiceImpl extends BaseServiceImpl<RoleGroup, RoleGroupDto> implements RoleGroupService {

    private final RoleGroupRepository roleGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FunctionRepository functionRepository;

    @Autowired
    MessageCommon messageCommon;

    public RoleGroupServiceImpl(RoleGroupRepository roleGroupRepository) {
        super(roleGroupRepository, RoleGroup.class, RoleGroupDto.class);
        this.roleGroupRepository = roleGroupRepository;
    }

    @Override
    public List<RoleGroupResponseDto> listRoleGroup() {
        return roleGroupRepository.getListRoleGroup();
    }

    @Override
    public void deleteRoleGroup(Long id) {
        RoleGroup group = roleGroupRepository.findById(id).orElse(null);
        if (group == null || group.getIsDeleted() == 1) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND));
        }

        int numberUser = userRepository.countUserByRoleGroupId(id);
        if (numberUser > 0) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_HAS_USER,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_HAS_USER));
        }
        group.setIsDeleted(1);
        roleGroupRepository.save(group);
    }

    public Long saveRoleGroup(RoleGroupDto dto) {
        checkSaveRoleGroup(dto);
        RoleGroup roleGroup = modelMapper.map(dto, RoleGroup.class);

        List<Function> functions = new ArrayList<>();
        if (Boolean.TRUE.equals(dto.getIsAdmin())) {
            List<Function> adminFunction = functionRepository.getAdminFunction();
            functions.addAll(adminFunction);
        } else {
            if (dto.getFunctionIds() != null && !dto.getFunctionIds().isEmpty()) {
                functions = functionRepository.findByIds(dto.getFunctionIds());
            }
        }
        roleGroup.setFunctions(functions);

        if (Boolean.TRUE.equals(dto.getDefaultGroup())) {
            roleGroup.setDefaultGroup(true);
        }
        roleGroupRepository.save(roleGroup);

        Set<UserEntity> users = new HashSet<>();
        if (Boolean.TRUE.equals(roleGroup.getDefaultGroup())) {
            users = findStaffsOfRoleGroupDefault(dto);
        }
        if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            users.addAll(userRepository.findByIds(dto.getUserIds()));
        }

        for (UserEntity staff : users) {
            roleGroupRepository.insertUserRoleGroup(roleGroup.getId(), staff.getId());
        }

        return roleGroup.getId();
    }

    @Override
    public Long updateRoleGroup(RoleGroupDto dto) {
        var roleGroup = checkUpdateRoleGroup(dto);

        roleGroup.setRoleGroupName(dto.getRoleGroupName());
        roleGroup.setDescription(dto.getDescription());
        int isActive = dto.getIsActive() ? 1 : 0;
        roleGroup.setIsActive(isActive);
        roleGroup.setIsAdmin(dto.getIsAdmin());
        List<Function> functions = new ArrayList<>();
        if (Boolean.TRUE.equals(dto.getIsAdmin())) {
            List<Function> adminFunction = functionRepository.getAdminFunction();
            functions.addAll(adminFunction);
        } else {
            if (dto.getFunctionIds() != null && !dto.getFunctionIds().isEmpty()) {
                functions = functionRepository.findByIds(dto.getFunctionIds());
            }
        }
        roleGroup.setFunctions(functions);

        if (Boolean.TRUE.equals(dto.getDefaultGroup())) {
            roleGroup.setDefaultGroup(true);
        }
        roleGroupRepository.save(roleGroup);

        Set<UserEntity> staffs = new HashSet<>();
        if (Boolean.TRUE.equals(roleGroup.getDefaultGroup())) {
            staffs = findStaffsOfRoleGroupDefault(dto);
        }
        if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            staffs.addAll(userRepository.findByIds(dto.getUserIds()));
        }

        roleGroupRepository.deleteUserRoleGroup(dto.getId());

        for (UserEntity staff : staffs) {
            roleGroupRepository.insertUserRoleGroup(roleGroup.getId(), staff.getId());
        }

        return roleGroup.getId();
    }

    private Set<UserEntity> findStaffsOfRoleGroupDefault(RoleGroupDto dto) {
        return null;
//        return new HashSet<>(userRepository.findAllByOrganizationsAndPositions(
//                new HashSet<>(dto.getOrganizationIds()), new HashSet<>(dto.getPositionIds())));
    }
    
    @Override
    public RoleGroupInfoResponseDto roleGroupInfo(Long roleGroupId) {
        RoleGroupInfoResponseDto roleGroupInfoResponseDto = new RoleGroupInfoResponseDto();
        RoleGroup roleGroup = roleGroupRepository.findById(roleGroupId).orElse(null);
        if (roleGroup == null) {
            return null;
        }
        RoleGroupDto roleGroupDto = modelMapper.map(roleGroup, RoleGroupDto.class);
        roleGroupInfoResponseDto.setRoleGroupDto(roleGroupDto);
        setUserResponse(roleGroup, roleGroupInfoResponseDto);
        if (!CollectionUtils.isEmpty(roleGroup.getFunctions())) {
            List<FunctionResponseDto> lsFunctions = roleGroup.getFunctions().stream()
                    .map(u -> modelMapper.map(u, FunctionResponseDto.class)).collect(Collectors.toList());
            roleGroupInfoResponseDto.setLsFunction(lsFunctions);
        }
        return roleGroupInfoResponseDto;
    }
    
    @Override
    public List<RoleGroupDto> findAllRoleGroupDefault() {
        List<RoleGroup> roleGroups = roleGroupRepository.findAllDefaultRoleGroup();

        List<RoleGroupDto> roleGroupDtos = new ArrayList<>();
        roleGroups.forEach(roleGroup -> {
            RoleGroupDto roleGroupDto = DataUtil.convertObject(roleGroup, x -> modelMapper.map(x, RoleGroupDto.class));

            roleGroupDto.setFunctionIds(roleGroup.getFunctions().stream().map(Function::getId).collect(Collectors.toList()));

            roleGroupDto.setUserIds(roleGroup.getUsers().stream().map(UserEntity::getId).collect(Collectors.toList()));

            roleGroupDtos.add(roleGroupDto);
        });

        return roleGroupDtos;
    }

    @Override
    public boolean existAdminRoleGroup() {
        return roleGroupRepository.getIdRoleGroupAdmin() != null;
    }

    @Override
    public Page<RoleGroupSearchResponse> searchRoleGroup(RoleGroupSearchRequest roleGroupSearchRequest, Pageable pageable) {
        roleGroupSearchRequest.setIsDeleted(0);
        return roleGroupRepository.searchRoleGroup(roleGroupSearchRequest, pageable);
    }

    @Override
    public Boolean deleteUserInGroup(RoleGroupDeleteUserDto roleGroupDto) {
        if (CollectionUtils.isEmpty(roleGroupDto.getUserDeleteIdList())) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_DELETE_USER_NULL,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_DELETE_USER_NULL));
        }
        roleGroupRepository.deleteUserRoleGroup(roleGroupDto.getRoleGroupId(), roleGroupDto.getUserDeleteIdList());

        RoleGroup group = roleGroupRepository.findById(roleGroupDto.getRoleGroupId()).orElse(null);
        if (group == null) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND));
        }
        SearchUserDto request = new SearchUserDto();
        request.setRoleIds(List.of(roleGroupDto.getRoleGroupId()));
//        Page<UserResponseDto> staffs = userService.searchStaffCustom(request, Pageable.ofSize(Integer.MAX_VALUE));

        return true;
    }

    @Override
    public boolean addStaffsToRoleGroupDefault(List<UserDto> staffDtos) {
        try {
            List<RoleGroupDto> roleGroupDtos = findAllRoleGroupDefault();
            Map<Long, List<Long>> insertStaffMaps = new HashMap<>();
            for (RoleGroupDto roleGroup : roleGroupDtos) {
                insertStaffMaps.put(roleGroup.getId(), new ArrayList<>());
                for (UserDto staffDto : staffDtos) {
//                    if (!CollectionUtils.isEmpty(organizationIds) && organizationIds.contains(staffDto.getOrganizationId())
//                            && !CollectionUtils.isEmpty(positionIds) && positionIds.contains(staffDto.getPositionId())) {
//                        if (roleGroupRepository.countUserRoleGroup(roleGroup.getId(), staffDto.getId()) == 0) {
//                            insertStaffMaps.get(roleGroup.getId()).add(staffDto.getId());
//                        }
//                    }
                }
            }

            insertStaffMaps.forEach((roleGroupId, staffIds) -> {
                for (Long staffId : staffIds) {
                    roleGroupRepository.insertUserRoleGroup(roleGroupId, staffId);
                }
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    @Override
    public Boolean checkAndDeleteUserFromDefaultGroup(List<Long> staffIds) {
//        List<UserDto> staffs = staffService.findByIds(staffIds);
//
//        List<RoleGroupDto> roleGroupDtos = findAllRoleGroupDefault();
//        for (RoleGroupDto roleGroup : roleGroupDtos) {
//            List<Long> deleteIds = new ArrayList<>();
//            for (StaffDto staff : staffs) {
//                List<Long> organizationIds = roleGroup.getOrganizationIds();
//                List<Long> positionIds = roleGroup.getPositionIds();
//                if (!CollectionUtils.isEmpty(organizationIds) && organizationIds.contains(staff.getOrganizationId())
//                        && !CollectionUtils.isEmpty(positionIds) && positionIds.contains(staff.getPositionId())) {
//                    deleteIds.add(staff.getId());
//                }
//            }
//            RoleGroupDeleteUserDto roleGroupDeleteUserDto = new RoleGroupDeleteUserDto();
//            roleGroupDeleteUserDto.setRoleGroupId(roleGroup.getId());
//            roleGroupDeleteUserDto.setUserDeleteIdList(deleteIds);
//            deleteUserInGroup(roleGroupDeleteUserDto);
//        }
        return true;
    }

    private RoleGroup checkUpdateRoleGroup(RoleGroupDto dto) {
        if (DataUtil.safeToLong(dto.getId()) == 0) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND,
                    messageCommon.getValueByMessageCode(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND));
        }
        RoleGroup roleGroup = roleGroupRepository.findById(dto.getId()).orElse(null);
        if (roleGroup == null || roleGroup.getIsDeleted() == 1) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_NOT_FOUND));
        }
        RoleGroup roleGroupNameExist = roleGroupRepository.findByRoleGroupName(dto.getRoleGroupName());
        if (roleGroupNameExist != null && !Objects.equals(roleGroupNameExist.getId(), dto.getId())) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NAME_ALREADY_EXISTS,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_NAME_ALREADY_EXISTS, dto.getRoleGroupName()));
        }
        if (Boolean.TRUE.equals(dto.getIsAdmin())) {
            Long adminId = roleGroupRepository.getIdRoleGroupAdmin();
            if (adminId != null && !Objects.equals(adminId, dto.getId())) {
                throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_ADMIN_ALREADY_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_ADMIN_ALREADY_EXISTS));
            }
        }

        return roleGroup;
    }

    private void checkSaveRoleGroup(RoleGroupDto dto) {
        RoleGroup roleGroupNameExist = roleGroupRepository.findByRoleGroupName(dto.getRoleGroupName());
        if (roleGroupNameExist != null) {
            throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_NAME_ALREADY_EXISTS,
                    messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_NAME_ALREADY_EXISTS, dto.getRoleGroupName()));
        }
        if (Boolean.TRUE.equals(dto.getIsAdmin())) {
            Long adminId = roleGroupRepository.getIdRoleGroupAdmin();
            if (adminId != null) {
                throw new BusinessException(BusinessExceptionCode.ROLE_GROUP_ADMIN_ALREADY_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.ROLE_GROUP_ADMIN_ALREADY_EXISTS));
            }
        }
    }

    private void setUserResponse(RoleGroup roleGroup, RoleGroupInfoResponseDto roleGroupInfoResponseDto) {
        if (!CollectionUtils.isEmpty(roleGroup.getUsers())) {
            List<String> listUserId = roleGroup.getUsers().stream().map(UserEntity::getUsername).collect(Collectors.toList());
            List<UserEntity> lsUsers = userRepository.findAllByUsernameIn(listUserId);
            roleGroupInfoResponseDto.setLsUser(DataUtil.convertList(lsUsers, x -> modelMapper.map(x, UserResponseDto.class)));
        }
    }

}
