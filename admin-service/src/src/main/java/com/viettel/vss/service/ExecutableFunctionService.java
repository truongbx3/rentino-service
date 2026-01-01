package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.executableFunc.ExecutableFunctionDto;
import com.viettel.vss.dto.executableFunc.FilterExecutableFuncRequest;
import com.viettel.vss.entity.ExecutableFunction;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExecutableFunctionService extends BaseService<ExecutableFunction, ExecutableFunctionDto>{
    ExecutableFunctionDto saveExecutableFunc(ExecutableFunctionDto executableFunctionDto);

    void deleteExecutableFunc(List<Long> ids);

    List<ExecutableFunctionDto> getExecutableFuncById(List<Long> ids);

    Page<ExecutableFunctionDto> getExecutableFuncs(FilterExecutableFuncRequest filterExecutableFuncRequest);
}