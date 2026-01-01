package com.viettel.vss.util;

import com.viettel.vss.dto.module.ModuleOrMenuResponseDto;
import com.viettel.vss.entity.Menu;
import com.viettel.vss.entity.Module;
import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtils {
    private MenuUtils() {

    }

    public static ModuleOrMenuResponseDto mapFromMenu(Menu menu, ModelMapper modelMapper, String locale) {
        ModuleOrMenuResponseDto moduleOrMenuResponseDto = modelMapper.map(menu, ModuleOrMenuResponseDto.class);
        moduleOrMenuResponseDto.setModuleId(menu.getModule() != null ? menu.getModule().getId() : null);
        moduleOrMenuResponseDto.setIcon(menu.getIcon());
        return moduleOrMenuResponseDto;
    }

    public static ModuleOrMenuResponseDto mapFromModule(Module module, String locale) {
        ModuleOrMenuResponseDto moduleOrMenuResponseDto = new ModuleOrMenuResponseDto();
        moduleOrMenuResponseDto.setModuleName(module.getModuleName());
        moduleOrMenuResponseDto.setDescription(module.getDescription());
        moduleOrMenuResponseDto.setIcon(module.getIcon());
        moduleOrMenuResponseDto.setUrl(module.getUrl());
        moduleOrMenuResponseDto.setIndexOrder(module.getIndexOrder());
        moduleOrMenuResponseDto.setIsShow(module.getIsShow());
        moduleOrMenuResponseDto.setParentId(module.getParentId());
        moduleOrMenuResponseDto.setIsActive(module.getIsActive() == 1);
        moduleOrMenuResponseDto.setModuleId(module.getId());
        return moduleOrMenuResponseDto;
    }

    public static void getModuleChildList(List<ModuleOrMenuResponseDto> allModuleList, ModuleOrMenuResponseDto parent,
                                          List<ModuleOrMenuResponseDto> originMenuList) {
        List<ModuleOrMenuResponseDto> listChild = new ArrayList<>();
        List<ModuleOrMenuResponseDto> childList = allModuleList.stream()
                .filter(item -> item.getParentId() != null && item.getParentId().equals(parent.getModuleId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childList)) {
            listChild.addAll(childList);
        }
        if (!CollectionUtils.isEmpty(originMenuList)) {
            List<ModuleOrMenuResponseDto> listMenuItems = originMenuList.stream()
                    .filter(menu -> parent.getModuleId().equals(menu.getModuleId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listMenuItems)) {
                listChild.addAll(listMenuItems);
            }
        }
        parent.setItems(listChild);
        for (ModuleOrMenuResponseDto menuDTO : childList) {
            getModuleChildList(allModuleList, menuDTO, originMenuList);
        }
    }

    public static void getModuleAllChildList(List<ModuleOrMenuResponseDto> allModuleList, ModuleOrMenuResponseDto parent,
                                             List<ModuleOrMenuResponseDto> originMenuList) {
        List<ModuleOrMenuResponseDto> listChild = new ArrayList<>();
        List<ModuleOrMenuResponseDto> childList = allModuleList.stream()
                .filter(item -> item.getParentId() != null && item.getParentId().equals(parent.getModuleId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childList)) {
            listChild.addAll(childList);
        }
        if (!CollectionUtils.isEmpty(originMenuList)) {
            List<ModuleOrMenuResponseDto> listMenuItems = originMenuList.stream()
                    .filter(menu -> parent.getModuleId().equals(menu.getModuleId()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listMenuItems)) {
                listChild.addAll(listMenuItems);
            }
        }
        parent.setItems(listChild);
        for (ModuleOrMenuResponseDto menuDTO : childList) {
            getModuleAllChildList(allModuleList, menuDTO, originMenuList);
        }
    }

    public static void getModuleChildListUnMenu(List<ModuleOrMenuResponseDto> allModuleList, ModuleOrMenuResponseDto parent) {
        List<ModuleOrMenuResponseDto> listChild = new ArrayList<>();
        List<ModuleOrMenuResponseDto> childList = allModuleList.stream()
                .filter(item -> item.getParentId() != null && item.getParentId().equals(parent.getModuleId()))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childList)) {
            listChild.addAll(childList);
        }
        parent.setItems(listChild);
        for (ModuleOrMenuResponseDto menuDTO : childList) {
            getModuleChildListUnMenu(allModuleList, menuDTO);
        }
    }
}
