package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.AppConfigDto;
import com.viettel.vss.entity.AppConfig;
import com.viettel.vss.repository.AppConfigRepository;
import com.viettel.vss.service.AppConfigService;
import com.viettel.vss.util.DataUtil;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfig, AppConfigDto> implements AppConfigService {
	private final AppConfigRepository appConfigRepository;

	public AppConfigServiceImpl( AppConfigRepository appConfigRepository) {
		super(appConfigRepository,AppConfig.class,AppConfigDto.class);
		this.appConfigRepository=appConfigRepository;
	}

	@Override
	public List<AppConfigDto> getConfigByConfigName(List<String> configName) {
		List<AppConfig> appConfigs = appConfigRepository.findByConfigNameInAndIsDeleted(configName, 0);
		return DataUtil.convertList(appConfigs, x -> modelMapper.map(x, AppConfigDto.class));
	}

	@Override
	public String createConfig(AppConfigDto appConfigDto, String username) {
		List<AppConfig> appConfig = DataUtil.convertList(Collections.singletonList(appConfigDto), x -> modelMapper.map(x, AppConfig.class));
		appConfigRepository.saveAll(appConfig);
		return "Done";
	}
}