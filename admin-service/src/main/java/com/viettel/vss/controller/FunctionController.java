package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.RequestDto;
import com.viettel.vss.dto.function.*;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.FunctionService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("functions")
public class FunctionController extends BaseController {
	private FunctionService functionService;

	public FunctionController(FunctionService functionService){
		super(functionService);
		this.functionService = functionService;
	}

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Thêm mới chức năng")
    @PostMapping("/save-function")
    public ResponseEntity<ResponseDto<FunctionDto>> saveFunction(HttpServletRequest httpServletRequest, @RequestBody FunctionDto functionDto) {
        return ResponseConfig.success(functionService.saveObject(functionDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật tính năng")
    @PostMapping("/update-function")
    public ResponseEntity<ResponseDto<FunctionDto>> updateFunction(HttpServletRequest httpServletRequest, @RequestBody FunctionDto functionDto) {
        return ResponseConfig.success(functionService.saveObject(functionDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa chức năng")
    @PostMapping("/delete-function")
    public ResponseEntity<ResponseDto<String>> deleteFunction(HttpServletRequest httpServletRequest, @RequestBody List<Long> ids) {
        functionService.deleteByIds(ids);
        return ResponseConfig.success("Done");
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Export danh sách chức năng")
    @PostMapping("/export")
    public ResponseEntity<Resource> export(HttpServletRequest httpServletRequest,
                                           @RequestBody FilterFunctionRequest filterFunctionRequest) throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append("Danh_sach_chuc_nang");
        fileName.append(".xlsx");
        ByteArrayInputStream byteStream = functionService.export(filterFunctionRequest);
        InputStreamResource in = new InputStreamResource(byteStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(in);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lấy danh sách tính năng theo list chức năng")
    @PostMapping("/get-list-function")
    public ResponseEntity<ResponseDto<List<FunctionResponseProjection>>> getListFunctionByListMenuId(HttpServletRequest httpServletRequest,
                                                                                                     @RequestBody List<Long> menuId) {
        return ResponseConfig.success(functionService.getListFunctionByListMenuId(menuId));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lấy danh sách tính năng")
    @PostMapping("/get-functions")
    public ResponseEntity<ResponseDto<Page<FunctionResponseDto>>> getListFunctions(HttpServletRequest httpServletRequest,
                                                                                          @RequestBody FilterFunctionRequest filterFunctionRequest) {
        return ResponseConfig.success(functionService.getListFunction(filterFunctionRequest));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @PostMapping({"/search-not-role-test"})
    public <T> ResponseEntity<ResponseDto<Page<T>>> search(HttpServletRequest httpServletRequest, @RequestBody RequestDto requestDto) {
        return ResponseConfig.success(this.baseService.findAll(requestDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Khôi phục tính năng đã bị xóa")
    @PostMapping("/restore-function")
    public ResponseEntity<?> restoreFunction(HttpServletRequest httpServletRequest, @RequestBody List<Long> id) {
        return ResponseConfig.success(functionService.restoreFunction(id));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Import chức năng")
    @PostMapping("/import-function")
    public ResponseEntity<ResponseDto<ImportResponseDto>> importDetailRaDraft(HttpServletRequest httpServletRequest, @RequestPart MultipartFile file) throws IOException, ParseException {
        return ResponseConfig.success(functionService.importFunction(file));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Download file import mẫu")
    @PostMapping(value = "download-import-template")
    public ResponseEntity<Resource> downloadImportTemplate(HttpServletRequest httpServletRequest) throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append("Danh_sach_chuc_nang");
        fileName.append(".xlsx");
        ByteArrayInputStream byteStream = functionService.exportTemplate();
        InputStreamResource in = new InputStreamResource(byteStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(in);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Lấy danh sách role")
    @PostMapping("/get-function-by-roles")
    public ResponseEntity<ResponseDto<List<FunctionResponseDto>>> getFunctionByRole(HttpServletRequest httpServletRequest, @RequestBody List<Long> roleIds) {
        return ResponseConfig.success(functionService.getFunctionByRole(roleIds));
    }
}