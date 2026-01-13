package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.BanksDto;
import com.viettel.vss.dto.DevicePriceDto;
import com.viettel.vss.dto.attach_file.AttachFileDTO;
import com.viettel.vss.entity.Banks;
import com.viettel.vss.entity.DevicePrice;

import java.util.List;

public interface BanksService extends BaseService<Banks, BanksDto> {
    List<BanksDto> getBanksList () ;
}
