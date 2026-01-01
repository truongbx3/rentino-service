package com.viettel.vss.service;

import java.util.List;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.SettingDto;
import com.viettel.vss.entity.Setting;
public interface SettingService extends BaseService<Setting, SettingDto>{
    List<SettingDto> getSetting(String username, String tableName);
    String createSetting(String username, String value, String tableName);
}