package com.viettel.vss.controller;

import com.viettel.vss.base.BaseController;
import com.viettel.vss.dto.ResponseDto;
import com.viettel.vss.dto.SettingDto;
import com.viettel.vss.service.SettingService;
import com.viettel.vss.util.ResponseConfig;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setting")
public class SettingController extends BaseController {
	private SettingService settingService;

	public SettingController(SettingService settingService){
		super(settingService);
		this.settingService = settingService;
	}

	@GetMapping("/get-config-table")
	public ResponseEntity<ResponseDto<List<SettingDto>>> getByTable(Principal principal , @RequestParam(required=false) String tableName){
		return ResponseConfig.success(settingService.getSetting(principal.getName(), tableName));
	}

	@PostMapping("/save-config-table")
	public ResponseEntity<ResponseDto<String>> saveConfig(Principal principal ,@RequestParam String tableName, @RequestParam String value){
		return ResponseConfig.success(settingService.createSetting(principal.getName(), value, tableName));
	}
}