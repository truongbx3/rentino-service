package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.DeviceCheckRequest;
import com.viettel.vss.dto.DeviceInfoDto;
import com.viettel.vss.dto.DeviceQuestionDto;
import com.viettel.vss.entity.DeviceInfo;
import com.viettel.vss.entity.DeviceQuestion;

import java.util.List;

public interface DeviceQuestionService extends BaseService<DeviceQuestion, DeviceQuestionDto> {

    List<DeviceQuestionDto> findAllQuestionByType(String type);
}
