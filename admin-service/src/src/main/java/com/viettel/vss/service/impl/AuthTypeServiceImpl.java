package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.AuthTypeDto;
import com.viettel.vss.entity.AuthType;
import com.viettel.vss.repository.AuthTypeRepository;
import com.viettel.vss.service.AuthTypeService;
import org.springframework.stereotype.Service;

@Service
public class AuthTypeServiceImpl extends BaseServiceImpl<AuthType, AuthTypeDto> implements AuthTypeService {

    private final AuthTypeRepository authTypeRepository;

    public AuthTypeServiceImpl(AuthTypeRepository authTypeRepository) {
        super(authTypeRepository, AuthType.class, AuthTypeDto.class);
        this.authTypeRepository = authTypeRepository;
    }
}