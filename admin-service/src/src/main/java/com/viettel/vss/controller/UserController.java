package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ImportDetailDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.UserDto;
import com.viettel.vss.dto.user.*;
import com.viettel.vss.dto.user.DetailInfoStaff;
import com.viettel.vss.dto.user.UserRequestDto;
import com.viettel.vss.dto.user.UserResponseDto;
import com.viettel.vss.dto.user.UserUpdateRequest;
import com.viettel.vss.service.UserService;
import com.viettel.vss.util.DateUtil;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController extends BaseController {
	private UserService userService;

	public UserController(UserService userService){
		super(userService);
		this.userService = userService;
	}

    @PreAuthorize("fileRole(#httpServletRequest)")
    @GetMapping("users-by-role-group")
    @Operation(summary = "Danh sách người dùng theo nhóm quyền")
    public ResponseEntity<ResponseDto<Page<UserResponseDto>>>
    getUsersByRoleGroup(HttpServletRequest httpServletRequest,
                        @ModelAttribute UserRequestDto userRequestDto,
                        @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Sort sort) {
        Pageable pageable = PageRequest.of(userRequestDto.getPage(), userRequestDto.getSize(), sort);
        Page<UserResponseDto> users = userService.getUsersByRoleGroup(userRequestDto, pageable);
        return ResponseConfig.success(users);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @GetMapping("user-info")
    @Operation(summary = "thong tin user theo id")
    public ResponseEntity<ResponseDto<DetailInfoStaff>> getUserInfo(HttpServletRequest httpServletRequest,
                                                                    @RequestParam Long userId) {
        return ResponseConfig.success(userService.getInfoAndFunctionUser(userId));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @PostMapping("update-user-role")
    @Operation(summary = "chinh sua quyen nguoi dung")
    public ResponseEntity<ResponseDto<Boolean>> updateInfoUser(HttpServletRequest httpServletRequest,
                                                               @RequestBody UserUpdateRequest userDTO) {
        return ResponseConfig.success(userService.updateInfoUser(userDTO));
    }

    @PostMapping("create-user")
    public ResponseEntity<ResponseDto<UserDto>> createUser(HttpServletRequest httpServletRequest,
                                                            @RequestBody UserDto userDto) {
        return ResponseConfig.success(userService.saveUser(userDto));
    }

    @PostMapping("update-user")
    public ResponseEntity<ResponseDto<UserDto>> updateUser(HttpServletRequest httpServletRequest,
                                                           @RequestBody UserDto userDto) {
        return ResponseConfig.success(userService.saveUser(userDto));
    }

    @PostMapping("filter-user")
    public ResponseEntity<ResponseDto<Page<UserListDto>>> filterUser(HttpServletRequest httpServletRequest,
                                                                     @RequestBody FilterUserRequest request,
                                                                     @RequestParam(defaultValue = "20") Integer size,
                                                                     @RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "updatedDate") String sortBy,
                                                                     @RequestParam(defaultValue = "DESC" )String sortDirection){
        return ResponseConfig.success(userService.filterUser(request, size, page, sortBy, sortDirection));
    }

    @GetMapping("detail-user")
    public ResponseEntity<ResponseDto<UserListDto>> getDetailUser(HttpServletRequest httpServletRequest,
                                                                     @RequestParam Long userId){
        return ResponseConfig.success(userService.getDetailUser(userId));
    }

    @PostMapping("export")
    public ResponseEntity<Resource> export(HttpServletRequest httpServletRequest,
                                           @RequestBody FilterUserRequest request,
                                           @RequestParam(defaultValue = "updatedDate") String sortBy,
                                           @RequestParam(defaultValue = "DESC" )String sortDirection) throws IOException {
        StringBuilder fileName = new StringBuilder();
        String exportDate = DateUtil.convertDateToString(new Date(), "yyyyMMdd_HHmm");
        fileName.append(String.format("users_%s", exportDate));
        fileName.append(".xlsx");
        InputStreamResource in = new InputStreamResource((InputStream) userService.export(request, sortBy, sortDirection));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(in);
    }

    @PostMapping("export-template")
    public ResponseEntity<Resource> exportTemplate() throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append("import-user-sample");
        fileName.append(".xlsx");
        InputStreamResource in = new InputStreamResource((InputStream) userService.exportTemplate());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(in);
    }

    @PostMapping(value = "import")
    public ResponseEntity<ResponseDto<ImportDetailDto>> importUser(HttpServletRequest httpServletRequest, @RequestPart MultipartFile file){
        return ResponseConfig.success(userService.importUser(file));
    }

    @Override
    @PostMapping("/deleteByIds")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDto<String>> deleteByIds(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        userService.deleteByIds(ids);
        return ResponseConfig.success("Done");
    }

    @PostMapping("recovery-password")
    public ResponseEntity<ResponseDto<String>> recoveryPassword(HttpServletRequest httpServletRequest, @RequestParam Long id){
        userService.recoveryPassword(id);
        return ResponseConfig.success("Recovery password successfully");
    }

    @PostMapping("lockOrUnlock")
    public ResponseEntity<ResponseDto<String>> lockOrUnlock(@RequestBody UserActionLogRequest userActionLogRequest){
        userService.lockOrUnlockUsers(userActionLogRequest);
        return ResponseConfig.success(userService.lockOrUnlockUsers(userActionLogRequest).toString());
    }

}