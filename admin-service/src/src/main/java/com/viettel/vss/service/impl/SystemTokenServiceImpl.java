package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.SystemTokenDto;
import com.viettel.vss.entity.SystemToken;
import com.viettel.vss.repository.SystemTokenRepository;
import com.viettel.vss.service.SystemTokenService;
import org.springframework.stereotype.Service;

@Service
public class SystemTokenServiceImpl extends BaseServiceImpl<SystemToken, SystemTokenDto> implements SystemTokenService {

    private final SystemTokenRepository systemTokenRepository;

    public SystemTokenServiceImpl(SystemTokenRepository systemTokenRepository) {
        super(systemTokenRepository, SystemToken.class, SystemTokenDto.class);
        this.systemTokenRepository = systemTokenRepository;
    }
}