package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.ImportDetailDto;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.UserImportDto;
import com.viettel.vss.dto.user.*;
import com.viettel.vss.dto.user.DetailInfoStaff;
import com.viettel.vss.dto.user.UserRequestDto;
import com.viettel.vss.dto.user.UserResponseDto;
import com.viettel.vss.dto.user.UserUpdateRequest;
import com.viettel.vss.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface UserService extends BaseService<UserEntity, UserDto>{
    Page<UserResponseDto> getUsersByRoleGroup(UserRequestDto userRequestDto, Pageable pageable);

    DetailInfoStaff getInfoAndFunctionUser(Long userId);

    UserDto saveUser(UserDto userDto);

    Page<UserListDto> filterUser(FilterUserRequest request, Integer size, Integer page, String sortBy, String sortDirection);

    UserListDto getDetailUser(Long userId);

    ByteArrayInputStream export(FilterUserRequest request, String sortBy, String sortDirection) throws IOException;

    ByteArrayInputStream exportTemplate() throws IOException;

    Boolean updateInfoUser(UserUpdateRequest userDTO);

    ImportDetailDto importUser(MultipartFile file);

    void saveImportUser(List<UserImportDto> content);

    void recoveryPassword(Long id);

    StringBuilder lockOrUnlockUsers(UserActionLogRequest userActionLogRequest);
}