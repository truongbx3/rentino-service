package com.viettel.vss.service;

import java.util.List;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.menu.MenuDto;
import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.menu.AddFunctionMenuDto;
import com.viettel.vss.dto.menu.DeleteMenuResponseDto;
import com.viettel.vss.dto.menu.MenuRequestDto;
import com.viettel.vss.entity.Menu;
public interface MenuService extends BaseService<Menu, MenuDto>{
    List<MenuDto> getAllMenu();

    MenuRequestDto getMenuById(Long id);

    Long updateMenu(MenuRequestDto menuRequestDto);

    Boolean deleteMenu(Long id);

    DeleteMenuResponseDto deleteListMenus(List<Long> ids);

    DeleteMenuResponseDto removeFunctionDto(List<FunctionResponseDto> removeFunctionMenuDto);

    Boolean addFunctionMenu(AddFunctionMenuDto addFunctionMenuDto);
}