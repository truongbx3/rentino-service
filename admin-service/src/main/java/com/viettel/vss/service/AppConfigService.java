package com.viettel.vss.service;

import java.util.List;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.AppConfigDto;
import com.viettel.vss.entity.AppConfig;
public interface AppConfigService extends BaseService<AppConfig, AppConfigDto>{
    List<AppConfigDto> getConfigByConfigName(List<String> configName);
    String createConfig(AppConfigDto appConfigDto, String username);
}