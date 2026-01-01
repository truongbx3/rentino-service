package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.SettingDto;
import com.viettel.vss.entity.Setting;
import com.viettel.vss.repository.SettingRepository;
import com.viettel.vss.service.SettingService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.StringUtils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl extends BaseServiceImpl<Setting, SettingDto> implements SettingService {
	private final SettingRepository settingRepository;

	public SettingServiceImpl( SettingRepository settingRepository) {
		super(settingRepository,Setting.class,SettingDto.class);
		this.settingRepository=settingRepository;
	}

	@Override
	public List<SettingDto> getSetting(String username, String tableName) {
		List<Setting> settings = new ArrayList<>();
		if (StringUtils.isNullOrEmpty(tableName)){
			settings = settingRepository.findByUsernameAndIsDeleted(username, 0);
		}else{
			settings = settingRepository.findByUsernameAndIsDeletedAndTableName(username, 0, tableName);
		}
		if (!ObjectUtils.isEmpty(settings)){
			return DataUtil.convertList(settings, x -> modelMapper.map(x, SettingDto.class));
		}
		return null;
	}

	@Override
	public String createSetting(String username, String value, String tableName) {
		List<Setting> settings = settingRepository.findByUsernameAndIsDeletedAndTableName(username, 0, tableName);
		Setting setting = new Setting();;
		if (!ObjectUtils.isEmpty(settings)){
			setting = settings.get(0);
		}
		setting.setConfig(value);
		setting.setStatus(1);
		setting.setTableName(tableName);
		settingRepository.save(setting);
		return "Done";
	}
}