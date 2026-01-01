package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.AuthTypeDto;
import com.viettel.vss.dto.RequestDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.AuthTypeService;
import com.viettel.vss.util.ResponseConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth-type")
public class AuthTypeController extends BaseController {
	private AuthTypeService authTypeService;

	public AuthTypeController(AuthTypeService authTypeService){
		super(authTypeService);
		this.authTypeService = authTypeService;
	}

    @Operation(summary = "get data by list condition ")
    @PostMapping({"/get-not-role"})
    public ResponseEntity<ResponseDto<Page<AuthTypeDto>>> getAll(HttpServletRequest httpServletRequest, @RequestBody RequestDto requestDto) {
        return ResponseConfig.success(authTypeService.findAll(requestDto));
    }
}