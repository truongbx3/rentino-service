package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.SystemAuthDto;
import com.viettel.vss.entity.SystemAuth;
import com.viettel.vss.repository.SystemAuthRepository;
import com.viettel.vss.service.SystemAuthService;
import org.springframework.stereotype.Service;

@Service
public class SystemAuthServiceImpl extends BaseServiceImpl<SystemAuth, SystemAuthDto> implements SystemAuthService {

    private final SystemAuthRepository systemAuthRepository;

    public SystemAuthServiceImpl(SystemAuthRepository systemAuthRepository) {
        super(systemAuthRepository, SystemAuth.class, SystemAuthDto.class);
        this.systemAuthRepository = systemAuthRepository;
    }
}