package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.LlmVersionDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.LlmVersionService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("llm-version")
public class LlmVersionController extends BaseController {
	private LlmVersionService llmVersionService;

	public LlmVersionController(LlmVersionService llmVersionService){
		super(llmVersionService);
		this.llmVersionService = llmVersionService;
	}

    @PreAuthorize("permitAll()")
    @Operation(summary = "Thêm mới danh mục LLM")
    @PostMapping("/save-llm")
    public ResponseEntity<ResponseDto<LlmVersionDto>> saveFunction(HttpServletRequest httpServletRequest, @RequestBody LlmVersionDto llmVersionDto) {
        return ResponseConfig.success(llmVersionService.saveObject(llmVersionDto));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Cập nhật danh mục LLM")
    @PostMapping("/update-llm")
    public ResponseEntity<ResponseDto<LlmVersionDto>> updateFunction(HttpServletRequest httpServletRequest, @RequestBody LlmVersionDto llmVersionDto) {
        return ResponseConfig.success(llmVersionService.saveObject(llmVersionDto));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "Xóa danh mục LLM")
    @PostMapping("/delete-llm")
    public ResponseEntity<ResponseDto<String>> deleteFunction(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        llmVersionService.deleteByIds(ids);
        return ResponseConfig.success("Done");
    }
}