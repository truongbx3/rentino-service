package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.*;
import com.viettel.vss.entity.DeviceInfo;

import java.util.List;

public interface DeviceInfoService extends BaseService<DeviceInfo, DeviceInfoDto> {
    public String generateUUid(String username);

    public String saveDeviceInfo(String username, DeviceInfoDto deviceInfoDtos);

    public String deviceCheck(DeviceCheckRequest deviceCheckRequest, String username);

    public DeviceInfoDto analyze(String userName, String transactionId) throws Exception;

    public List<DeviceCheckDto> getDeviceCheck(String transactionId) ;
}
