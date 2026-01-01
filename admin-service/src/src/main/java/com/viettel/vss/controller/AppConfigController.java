package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.AppConfigDto;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.service.AppConfigService;
import com.viettel.vss.util.ResponseConfig;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app-config")
public class AppConfigController extends BaseController {
	private AppConfigService appConfigService;

	public AppConfigController(AppConfigService appConfigService){
		super(appConfigService);
		this.appConfigService = appConfigService;
	}

	@GetMapping("/get-by-config-code")
	public ResponseEntity<ResponseDto<List<AppConfigDto>>> getByConfigCode(@RequestParam List<String> configName){
		return ResponseConfig.success(appConfigService.getConfigByConfigName(configName));
	}

	@PostMapping("/create")
	public ResponseEntity<ResponseDto<String>> createAppConfig(@RequestBody AppConfigDto appConfigDto,Principal principal){
		return ResponseConfig.success(appConfigService.createConfig(appConfigDto, principal.getName()));
	}
}