package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.menu.AddFunctionMenuDto;
import com.viettel.vss.dto.menu.DeleteMenuResponseDto;
import com.viettel.vss.dto.menu.MenuRequestDto;
import com.viettel.vss.service.MenuService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController extends BaseController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        super(menuService);
        this.menuService = menuService;
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Cập nhật chức năng")
    @PostMapping("/update-menu")
    public ResponseEntity<ResponseDto<Long>> updateMenu(HttpServletRequest httpServletRequest,
                                                        @RequestBody MenuRequestDto menuRequestDto) {
        return ResponseConfig.success(menuService.updateMenu(menuRequestDto));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa chức năng")
    @PostMapping("/delete-menu")
    public ResponseEntity<ResponseDto<DeleteMenuResponseDto>> deleteMenu(HttpServletRequest httpServletRequest,
                                                                         @RequestBody List<Long> ids) {
        return ResponseConfig.success(menuService.deleteListMenus(ids));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Xóa tính năng khỏi chức năng")
    @PostMapping("/remove-function-menu")
    public ResponseEntity<ResponseDto<DeleteMenuResponseDto>> removeFunction(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid List<FunctionResponseDto> removeFunctionMenuDto) {
        return ResponseConfig.success(menuService.removeFunctionDto(removeFunctionMenuDto));
    }

    @Operation(summary = "Lấy menu theo id")
    @GetMapping("/get-menu/{id}")
    public ResponseEntity<ResponseDto<MenuRequestDto>> getMenuById(HttpServletRequest httpServletRequest, @PathVariable Long id) {
        return ResponseConfig.success(menuService.getMenuById(id));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Gán tính năng vào chức năng")
    @PostMapping("/add-function-menu")
    public ResponseEntity<ResponseDto<Boolean>> addFunctionMenu(HttpServletRequest httpServletRequest,
                                                                @RequestBody @Valid AddFunctionMenuDto addFunctionMenuDto) {
        return ResponseConfig.success(menuService.addFunctionMenu(addFunctionMenuDto));
    }
}