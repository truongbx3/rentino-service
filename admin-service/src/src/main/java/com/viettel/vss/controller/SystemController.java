package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.system.FilterSystemRequest;
import com.viettel.vss.dto.system.SystemDto;
import com.viettel.vss.dto.system.SystemResponseDto;
import com.viettel.vss.entity.System;
import com.viettel.vss.service.SystemService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("systems")
public class SystemController extends BaseController {
	private SystemService systemService;

	public SystemController(SystemService systemService){
		super(systemService);
		this.systemService = systemService;
	}

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Thêm mới hệ thống")
    @PostMapping("/save-system")
    public ResponseEntity<ResponseDto<System>> saveSystem(HttpServletRequest httpServletRequest, @RequestBody SystemDto systemDto) {
        return ResponseConfig.success(systemService.saveSystem(systemDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hệ thống")
    @PostMapping("/update-system")
    public ResponseEntity<ResponseDto<System>> updateSystem(HttpServletRequest httpServletRequest, @RequestBody SystemDto systemDto) {
        return ResponseConfig.success(systemService.saveSystem(systemDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa hệ thống")
    @PostMapping("/delete-system")
    public ResponseEntity<ResponseDto<String>> deleteSystem(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        systemService.deleteSystem(ids);
        return ResponseConfig.success("Done");
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hệ thống")
    @PostMapping("/get-systems")
    public ResponseEntity<ResponseDto<Page<SystemDto>>> getSystems(HttpServletRequest httpServletRequest, @RequestBody FilterSystemRequest filterSystemRequest) {
        return ResponseConfig.success(systemService.getSystems(filterSystemRequest));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hệ thống")
    @GetMapping("/get-system-by-id")
    public ResponseEntity<ResponseDto<List<SystemResponseDto>>> getSystemById(HttpServletRequest httpServletRequest, @RequestParam List<Long> ids) {
        return ResponseConfig.success(systemService.getSystemById(ids));
    }
}