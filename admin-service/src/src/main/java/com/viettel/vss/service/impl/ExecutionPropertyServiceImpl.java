package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.ExecutionPropertyDto;
import com.viettel.vss.entity.ExecutionProperty;
import com.viettel.vss.repository.ExecutionPropertyRepository;
import com.viettel.vss.service.ExecutionPropertyService;
import org.springframework.stereotype.Service;

@Service
public class ExecutionPropertyServiceImpl extends BaseServiceImpl<ExecutionProperty, ExecutionPropertyDto> implements ExecutionPropertyService {

    private final ExecutionPropertyRepository executionPropertyRepository;

    public ExecutionPropertyServiceImpl(ExecutionPropertyRepository executionPropertyRepository) {
        super(executionPropertyRepository, ExecutionProperty.class, ExecutionPropertyDto.class);
        this.executionPropertyRepository = executionPropertyRepository;
    }
}