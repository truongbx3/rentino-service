package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.system.FilterSystemRequest;
import com.viettel.vss.dto.system.SystemDto;
import com.viettel.vss.dto.system.SystemResponseDto;
import com.viettel.vss.entity.System;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SystemService extends BaseService<System, SystemDto>{
    Page<SystemDto> getSystems(FilterSystemRequest filterSystemRequest);

    System saveSystem(SystemDto systemDto);

    void deleteSystem(List<Long> ids);

    List<SystemResponseDto> getSystemById(List<Long> ids);
}