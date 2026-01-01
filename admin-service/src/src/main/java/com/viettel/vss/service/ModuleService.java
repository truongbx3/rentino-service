package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.module.*;
import com.viettel.vss.entity.Module;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ModuleService extends BaseService<Module, ModuleDto>{

    List<ModuleOrMenuResponseDto> getListModule(String locale);

    Long saveModuleMenu(ModuleDto moduleDto);

    Boolean deleteModule(Long id);

    DeleteModuleResponseDto deleteListModule(List<Long> ids);

    Long updateModule(ModuleRequestDto moduleRequestDto);

    ModuleRequestDto getModuleById(Long id);

    List<ModuleOrMenuResponseDto> getAllMenu(String locale);

    List<ModuleOrMenuResponseAlwaysVisibleDto> getListMenuShow(HttpServletRequest httpServletRequest, String locale);
}