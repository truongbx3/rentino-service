package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.module.*;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.ModuleService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("modules")
public class ModuleController extends BaseController {
    private final ModuleService moduleService;

	public ModuleController(ModuleService moduleService){
		super(moduleService);
		this.moduleService = moduleService;
	}

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lấy danh sách phân hệ / chức năng")
    @GetMapping("/get-module-menu")
    public ResponseEntity<ResponseDto<List<ModuleOrMenuResponseDto>>> getListModule(HttpServletRequest httpServletRequest,
                                                                                    @RequestParam(required = false, defaultValue = "vi") String locale) {
        return ResponseConfig.success(moduleService.getListModule(locale));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Thêm mới hệ / chức năng")
    @PostMapping("/save-module-menu")
    public ResponseEntity<ResponseDto<Long>> saveModuleMenu(HttpServletRequest httpServletRequest,
                                                            @Valid @RequestBody ModuleDto moduleDto) {
        return ResponseConfig.success(moduleService.saveModuleMenu(moduleDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật phân hệ")
    @PostMapping("/update-module")
    public ResponseEntity<ResponseDto<Long>> updateModule(HttpServletRequest httpServletRequest,
                                                          @RequestBody ModuleRequestDto moduleRequestDto) {
        return ResponseConfig.success(moduleService.updateModule(moduleRequestDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa phân hệ")
    @PostMapping("/delete-module")
    public ResponseEntity<ResponseDto<DeleteModuleResponseDto>> deleteModule(HttpServletRequest httpServletRequest,
                                                                             @RequestBody List<Long> ids) {
        return ResponseConfig.success(moduleService.deleteListModule(ids));
    }

    @Operation(summary = "Lấy danh sách menu để hiển thị cho người dùng")
    @GetMapping("/get-menu-show")
    public ResponseEntity<ResponseDto<List<ModuleOrMenuResponseAlwaysVisibleDto>>> getMenuShowView(HttpServletRequest httpServletRequest,
                                                                                                   @RequestParam(required = false, defaultValue = "vi") String locale) {
        return ResponseConfig.success(moduleService.getListMenuShow(httpServletRequest, locale));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @GetMapping("/get-all-menu")
    @Operation(summary = "Lấy danh sách menu all")
    public ResponseEntity<ResponseDto<List<ModuleOrMenuResponseDto>>> getAllMenu(HttpServletRequest httpServletRequest, @RequestParam(required = false, defaultValue = "vi") String locale) {
        return ResponseConfig.success(moduleService.getAllMenu(locale));
    }

    @GetMapping("/get-module/{id}")
    @Operation(summary = "Lấy phân hệ theo id")
    public ResponseEntity<ResponseDto<ModuleRequestDto>> getModuleById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return ResponseConfig.success(moduleService.getModuleById(id));
    }
}