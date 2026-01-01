package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.function.*;
import com.viettel.vss.dto.module.DeleteModuleResponseDto;
import com.viettel.vss.entity.Function;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface FunctionService extends BaseService<Function, FunctionDto>{
    ByteArrayInputStream export(FilterFunctionRequest filterFunctionRequest) throws IOException;

    List<FunctionResponseProjection> getListFunctionByListMenuId(List<Long> menuId);

    Page<FunctionResponseDto> getListFunction(FilterFunctionRequest filterFunctionRequest);

    DeleteModuleResponseDto restoreFunction(List<Long> id);

    ImportResponseDto importFunction(MultipartFile file) throws IOException, ParseException;

    ByteArrayInputStream exportTemplate() throws IOException;

    List<FunctionResponseDto> getFunctionByRole(List<Long> roleIds);

    List<String> getMyRole(String email);
}