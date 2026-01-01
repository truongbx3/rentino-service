package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.roleGroup.*;
import com.viettel.vss.service.RoleGroupService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("role-groups")
public class RoleGroupController extends BaseController {
    private final RoleGroupService roleGroupService;

    public RoleGroupController(RoleGroupService roleGroupService) {
        super(roleGroupService);
        this.roleGroupService = roleGroupService;
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lấy danh sách nhóm quyền và số người dùng thuộc nhóm quyền")
    @GetMapping()
    public ResponseEntity<ResponseDto<List<RoleGroupResponseDto>>> getListRoleGroup(HttpServletRequest httpServletRequest) {
        return ResponseConfig.success(roleGroupService.listRoleGroup());
    }

    @Operation(summary = "Xóa 1 nhóm quyền")
    @PostMapping("/delete-role-group/{id}")
    public ResponseEntity<ResponseDto<Boolean>> deleteRoleGroup(HttpServletRequest httpServletRequest,
                                                                @PathVariable("id") Long roleGroupId) {
        roleGroupService.deleteRoleGroup(roleGroupId);
        return ResponseConfig.success(true);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lưu dữ liệu nhóm quyền")
    @PostMapping("/save-role-group")
    public ResponseEntity<ResponseDto<Long>> saveRoleGroup(HttpServletRequest httpServletRequest,
                                                           @RequestBody @Valid RoleGroupDto roleGroupDto) {
        return ResponseConfig.success(roleGroupService.saveRoleGroup(roleGroupDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Kiểm tra tồn tại nhóm quyền admin")
    @PostMapping("/exist-admin-role-group")
    public ResponseEntity<ResponseDto<Boolean>> existAdminRoleGroup(HttpServletRequest httpServletRequest) {
        return ResponseConfig.success(roleGroupService.existAdminRoleGroup());
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật dữ liệu nhóm quyền")
    @PostMapping("/update-role-group")
    public ResponseEntity<ResponseDto<Long>> updateRoleGroup(HttpServletRequest httpServletRequest,
                                                             @RequestBody @Valid RoleGroupDto roleGroupDto) {
        return ResponseConfig.success(roleGroupService.updateRoleGroup(roleGroupDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "thong tin nhom quyen")
    @GetMapping("/role-group-info")
    public ResponseEntity<ResponseDto<RoleGroupInfoResponseDto>> roleGroupInfo(HttpServletRequest httpServletRequest,
                                                                               @RequestParam Long roleGroupId) {
        return ResponseConfig.success(roleGroupService.roleGroupInfo(roleGroupId));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Tìm kiếm nhóm quyền")
    @GetMapping("/search-role-group")
    public ResponseEntity<ResponseDto<Page<RoleGroupSearchResponse>>> searchRoleGroup(HttpServletRequest httpServletRequest,
                                                                                      RoleGroupSearchRequest roleGroupSearchRequest,
                                                                                      @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(roleGroupSearchRequest.getPage(), roleGroupSearchRequest.getSize(), sort);
        return ResponseConfig.success(roleGroupService.searchRoleGroup(roleGroupSearchRequest, pageable));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa người dùng khỏi nhóm quyền")
    @PostMapping("/delete-user-role-group")
    public ResponseEntity<ResponseDto<Boolean>> deleteUserInGroup(HttpServletRequest httpServletRequest,
                                                                  @RequestBody @Valid RoleGroupDeleteUserDto roleGroupDto) {
        return ResponseConfig.success(roleGroupService.deleteUserInGroup(roleGroupDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "add người dùng vào nhóm quyền mặc định")
    @PostMapping("/add-staff-role-group-default")
    public ResponseEntity<ResponseDto<Boolean>> addStaffToRoleGroupDefault(HttpServletRequest httpServletRequest,
                                                                           @RequestBody @Valid List<UserDto> userDtos ) {
        return ResponseConfig.success(roleGroupService.addStaffsToRoleGroupDefault(userDtos));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "kiem tra xoa nguoi dung tu group mac dinh")
    @PostMapping("/check-delete-user-default")
    public ResponseEntity<ResponseDto<Boolean>> checkAndDeleteUserFromDefaultGroup(HttpServletRequest httpServletRequest,
                                                                                   @RequestBody @Valid List<Long> ids ) {
        return ResponseConfig.success(roleGroupService.checkAndDeleteUserFromDefaultGroup(ids));
    }
}