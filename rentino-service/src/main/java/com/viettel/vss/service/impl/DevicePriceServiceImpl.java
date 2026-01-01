package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.DevicePriceDto;
import com.viettel.vss.dto.DeviceQuestionAnswerDto;
import com.viettel.vss.dto.DeviceQuestionDto;
import com.viettel.vss.entity.DevicePrice;
import com.viettel.vss.entity.DeviceQuestion;
import com.viettel.vss.repository.DevicePriceRepository;
import com.viettel.vss.repository.DeviceQuestionRepository;
import com.viettel.vss.service.DevicePriceService;
import com.viettel.vss.service.DeviceQuestionService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DevicePriceServiceImpl extends BaseServiceImpl<DevicePrice, DevicePriceDto> implements DevicePriceService {


    @Autowired
    private MessageCommon messageCommon;
    private final DevicePriceRepository devicePriceRepository;

    public DevicePriceServiceImpl(DevicePriceRepository devicePriceRepository) {
        super(devicePriceRepository, DevicePrice.class, DevicePriceDto.class);
        this.devicePriceRepository = devicePriceRepository;
    }

    @Override
    public List<DevicePriceDto> getDevices() throws Exception {
        return  DataUtil.convertList(devicePriceRepository.findAllByIsDeleted(0), x -> modelMapper.map(x, DevicePriceDto.class));
    }
}
