package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ExecutionTypeDto;
import com.viettel.vss.dto.RequestDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.ExecutionTypeService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("execution-type")
public class ExecutionTypeController extends BaseController {
	private ExecutionTypeService executionTypeService;

	public ExecutionTypeController(ExecutionTypeService executionTypeService){
		super(executionTypeService);
		this.executionTypeService = executionTypeService;
	}

    @PreAuthorize("permitAll()")
    @Operation(summary = "Thêm mới danh mục Loại hàm thực thi")
    @PostMapping("/save-execution-type")
    public ResponseEntity<ResponseDto<ExecutionTypeDto>> saveFunction(HttpServletRequest httpServletRequest, @RequestBody ExecutionTypeDto executionTypeDto) {
        return ResponseConfig.success(executionTypeService.saveObject(executionTypeDto));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Cập nhật danh mục Loại hàm thực thi")
    @PostMapping("/update-execution-type")
    public ResponseEntity<ResponseDto<ExecutionTypeDto>> updateFunction(HttpServletRequest httpServletRequest, @RequestBody ExecutionTypeDto executionTypeDto) {
        return ResponseConfig.success(executionTypeService.saveObject(executionTypeDto));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Xóa danh mục Loại hàm thực thi")
    @PostMapping("/delete-execution-type")
    public ResponseEntity<ResponseDto<String>> deleteFunction(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        executionTypeService.deleteByIds(ids);
        return ResponseConfig.success("Done");
    }

    @Operation(summary = "get data by list condition ")
    @PostMapping({"/get-not-role"})
    public ResponseEntity<ResponseDto<Page<ExecutionTypeDto>>> getAll(HttpServletRequest httpServletRequest, @RequestBody RequestDto requestDto) {
        return ResponseConfig.success(executionTypeService.findAll(requestDto));
    }
}