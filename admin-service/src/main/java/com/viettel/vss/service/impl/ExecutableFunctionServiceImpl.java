package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.executableFunc.ExecutableFunctionDto;
import com.viettel.vss.dto.executableFunc.FilterExecutableFuncRequest;
import com.viettel.vss.entity.ExecutableFunction;
import com.viettel.vss.entity.ExecutionType;
import com.viettel.vss.entity.System;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.ExecutableFunctionRepository;
import com.viettel.vss.repository.ExecutionTypeRepository;
import com.viettel.vss.repository.SystemRepository;
import com.viettel.vss.service.ExecutableFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ExecutableFunctionServiceImpl extends BaseServiceImpl<ExecutableFunction, ExecutableFunctionDto> implements ExecutableFunctionService {

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private ExecutionTypeRepository executionTypeRepository;

    private final ExecutableFunctionRepository executableFunctionRepository;

    public ExecutableFunctionServiceImpl(ExecutableFunctionRepository executableFunctionRepository) {
        super(executableFunctionRepository, ExecutableFunction.class, ExecutableFunctionDto.class);
        this.executableFunctionRepository = executableFunctionRepository;
    }

    @Override
    public ExecutableFunctionDto saveExecutableFunc(ExecutableFunctionDto data) {
        if (!ObjectUtils.isEmpty(data.getSystemId())) {
            System system = systemRepository.findByIdAndIsDeleted(data.getSystemId(), 0);
            if (ObjectUtils.isEmpty(system)) {
                throw new BusinessException(BusinessExceptionCode.SYSTEM_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.SYSTEM_NOT_FOUND));
            }
            data.setSystem(system);
        }

        if (!ObjectUtils.isEmpty(data.getExecutionTypeId())) {
            ExecutionType executionType = executionTypeRepository.findByIdAndIsDeleted(data.getSystemId(), 0);
            if (ObjectUtils.isEmpty(executionType)) {
                throw new BusinessException(BusinessExceptionCode.EXECUTION_TYPE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.EXECUTION_TYPE_NOT_FOUND));
            }
            data.setExecutionType(executionType);
        }

        if (!ObjectUtils.isEmpty(data.getMetaData())) {
            validateMetadata(data);
        }

        if (!ObjectUtils.isEmpty(data.getId())) {
            ExecutableFunction executableFunction = executableFunctionRepository.findByIdAndIsDeleted(data.getId(), 0);

            if (ObjectUtils.isEmpty(executableFunction)) {
                throw new BusinessException(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND));
            }

            data.setId(executableFunction.getId());
            data.setCreatedBy(executableFunction.getCreatedBy());
            data.setCreatedDate(executableFunction.getCreatedDate());
        }

        return super.saveObject(data);
    }

    @Override
    public void deleteExecutableFunc(List<Long> ids) {
        List<ExecutableFunction> data = executableFunctionRepository.findAllByIdInAndIsDeleted(ids, 0);
        if (ObjectUtils.isEmpty(data)) {
            throw new BusinessException(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND));
        }

        //TODO: Check đang sử dụng

        super.deleteByIds(ids);
    }

    @Override
    public List<ExecutableFunctionDto> getExecutableFuncById(List<Long> ids) {
        List<ExecutableFunctionDto> result = executableFunctionRepository.getExecutableFuncById(ids, 0);
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.EXECUTABLE_FUNC_NOT_FOUND));
        }

        return result;
    }

    @Override
    public Page<ExecutableFunctionDto> getExecutableFuncs(FilterExecutableFuncRequest filterExecutableFuncRequest) {
        Sort.Direction direction = Sort.Direction.fromString(filterExecutableFuncRequest.getSortDirection());
        Pageable pageable = PageRequest.of(filterExecutableFuncRequest.getPage(),
                filterExecutableFuncRequest.getSize(),
                Sort.by(direction, filterExecutableFuncRequest.getSortBy()));
        return executableFunctionRepository.getExecutableFuncs(filterExecutableFuncRequest, pageable);
    }

    private void validateMetadata(ExecutableFunctionDto data) {
    }

}