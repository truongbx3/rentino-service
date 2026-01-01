package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.ExecutionTypeDto;
import com.viettel.vss.entity.ExecutionType;
import com.viettel.vss.repository.ExecutionTypeRepository;
import com.viettel.vss.service.ExecutionTypeService;
import org.springframework.stereotype.Service;

@Service
public class ExecutionTypeServiceImpl extends BaseServiceImpl<ExecutionType, ExecutionTypeDto> implements ExecutionTypeService {

    private final ExecutionTypeRepository executionTypeRepository;

    public ExecutionTypeServiceImpl(ExecutionTypeRepository executionTypeRepository) {
        super(executionTypeRepository, ExecutionType.class, ExecutionTypeDto.class);
        this.executionTypeRepository = executionTypeRepository;
    }
}