package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.executableFunc.ExecutableFunctionDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.executableFunc.FilterExecutableFuncRequest;
import com.viettel.vss.service.ExecutableFunctionService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("executable-functions")
public class ExecutableFunctionController extends BaseController {
	private ExecutableFunctionService executableFunctionService;

	public ExecutableFunctionController(ExecutableFunctionService executableFunctionService){
		super(executableFunctionService);
		this.executableFunctionService = executableFunctionService;
	}

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Thêm mới hàm thực thi")
    @PostMapping("/save")
    public ResponseEntity<ResponseDto<ExecutableFunctionDto>> saveExecutableFunc(HttpServletRequest httpServletRequest, @RequestBody ExecutableFunctionDto executableFunctionDto) {
        return ResponseConfig.success(executableFunctionService.saveExecutableFunc(executableFunctionDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hàm thực thi")
    @PostMapping("/update")
    public ResponseEntity<ResponseDto<ExecutableFunctionDto>> updateExecutableFunc(HttpServletRequest httpServletRequest, @RequestBody ExecutableFunctionDto executableFunctionDto) {
        return ResponseConfig.success(executableFunctionService.saveExecutableFunc(executableFunctionDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa hàm thực thi")
    @PostMapping("/delete")
    public ResponseEntity<ResponseDto<String>> deleteExecutableFunc(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        executableFunctionService.deleteExecutableFunc(ids);
        return ResponseConfig.success("Done");
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hệ thống")
    @PostMapping("/get-executable-funcs")
    public ResponseEntity<ResponseDto<Page<ExecutableFunctionDto>>> getExecutableFuncs(HttpServletRequest httpServletRequest, @RequestBody FilterExecutableFuncRequest filterExecutableFuncRequest) {
        return ResponseConfig.success(executableFunctionService.getExecutableFuncs(filterExecutableFuncRequest));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật hệ thống")
    @GetMapping("/get-by-id")
    public ResponseEntity<ResponseDto<List<ExecutableFunctionDto>>> getExecutableFuncById(HttpServletRequest httpServletRequest, @RequestParam List<Long> ids) {
        return ResponseConfig.success(executableFunctionService.getExecutableFuncById(ids));
    }
}