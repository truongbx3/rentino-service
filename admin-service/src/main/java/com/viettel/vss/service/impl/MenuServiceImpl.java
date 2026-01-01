package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.menu.MenuDto;
import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.menu.*;
import com.viettel.vss.entity.Function;
import com.viettel.vss.entity.Menu;
import com.viettel.vss.entity.Module;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.FunctionRepository;
import com.viettel.vss.repository.MenuRepository;
import com.viettel.vss.repository.ModuleRepository;
import com.viettel.vss.service.MenuService;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu, MenuDto> implements MenuService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    MessageCommon messageCommon;

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        super(menuRepository, Menu.class, MenuDto.class);
        this.menuRepository = menuRepository;
    }

    @Override
    public List<MenuDto> getAllMenu() {
        List<MenuDto> menuDtos = new ArrayList<>();
        List<Menu> menus = menuRepository.findByIsDeleted(0);
        List<MenuDto> lstMenu = DataUtil.convertList(menus, x -> modelMapper.map(x, MenuDto.class));
        Map<Long, List<MenuDto>> mapMenu = lstMenu.stream()
                .filter(x -> x.getParentId() != null)
                .collect(groupingBy(MenuDto::getParentId));
        for (MenuDto menu : lstMenu) {
            if (menu.getParentId() == null) {
                menuDtos.add(menu);
            }
            List<MenuDto> lstChild = mapMenu.get(menu.getId());
            if (lstChild != null) {
                menu.setMenuDtos(lstChild);
            }
        }
        return menuDtos;
    }

    @Override
    public MenuRequestDto getMenuById(Long id) {
        MenuRequestDto result = new MenuRequestDto();
        Menu menu = menuRepository.getReferenceById(id);
        modelMapper.map(menu, result);
        result.setModuleId(menu.getModule() != null ? menu.getModule().getId() : null);
        if (result.getModuleId() != null) {
            var module = moduleRepository.getReferenceById(result.getModuleId());
            result.setModuleName(module.getModuleName());
        }
        return result;
    }

    @Override
    public Long updateMenu(MenuRequestDto menuRequestDto) {
        if (!ObjectUtils.isEmpty(menuRequestDto.getIcon()) && !ValidationUtils.isValidSvgUrl(menuRequestDto.getIcon())) {
            throw new BusinessException(BusinessExceptionCode.MODULE_INVALID_ICON,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_INVALID_ICON));
        }

        Menu menu = menuRepository.findById(menuRequestDto.getId()).orElse(null);
        if (menu == null) {
            throw new BusinessException(BusinessExceptionCode.MENU_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.MENU_NOT_FOUND));
        } else if (!ObjectUtils.isEmpty(menu.getModule())){
            if (!menu.getMenuName().equalsIgnoreCase(menuRequestDto.getMenuName()) &&
                    !menuRepository.checkExistMenuName(menuRequestDto.getMenuName().toUpperCase(), menu.getId()).isEmpty()) {
                throw new BusinessException(BusinessExceptionCode.MENU_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.MENU_EXISTS, menuRequestDto.getMenuName()));
            }
        }

        modelMapper.map(menuRequestDto, menu);
        if (menuRequestDto.getModuleId() != null) {
            Module module = moduleRepository.getReferenceById(menuRequestDto.getModuleId());
            menu.setModule(module);
        }else{
            menu.setModule(null);
        }
        menuRepository.save(menu);
        return menu.getId();
    }

    @Override
    public Boolean deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu == null || menu.getIsDeleted() == 1) {
            throw new BusinessException(BusinessExceptionCode.MENU_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.MENU_NOT_FOUND));
        }
        Integer countFunction = menuRepository.countFunctionOfMenu(id);
        if (countFunction > 0) {
            throw new BusinessException(BusinessExceptionCode.MENU_HAS_FUNCTION,
                    messageCommon.getMessage(BusinessExceptionCode.MENU_HAS_FUNCTION, menu.getMenuName()));
        }
        menu.setIsDeleted(1);
        menuRepository.save(menu);
        return true;
    }

    @Override
    public DeleteMenuResponseDto deleteListMenus(List<Long> ids) {
        DeleteMenuResponseDto responseDto = new DeleteMenuResponseDto();
        List<String> error = new ArrayList<>();
        boolean isContinue;
        for (Long id : ids) {
            isContinue = false;
            Menu menu = menuRepository.findById(id).orElse(null);
            if (menu == null || menu.getIsDeleted() == 1) {
                error.add(messageCommon.getMessage(BusinessExceptionCode.MENU_NOT_FOUND));
                isContinue = true;
            } else {
                Integer countFunction = menuRepository.countFunctionOfMenu(id);
                if (countFunction > 0) {
                    error.add(messageCommon.getMessage(BusinessExceptionCode.MENU_HAS_FUNCTION, menu.getMenuName()));
                    isContinue = true;
                }
            }
            if (isContinue) {
                continue;
            }

            menu.setIsDeleted(1);
            menuRepository.save(menu);
        }

        if (error.isEmpty()) {
            responseDto.setIsSuccess(true);
        } else {
            responseDto.setIsSuccess(false);
            responseDto.setError(error);
        }
        return responseDto;
    }

    @Override
    public DeleteMenuResponseDto removeFunctionDto(List<FunctionResponseDto> functionResponseDtoList) {
        DeleteMenuResponseDto responseDto = new DeleteMenuResponseDto();
        List<RemoveFunctionMenuDto> removeFunctionMenuDtoList = new ArrayList<>();

        List<Long> listMenu = functionResponseDtoList.stream().map(FunctionResponseDto::getMenuId).distinct().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(listMenu)) {
            for (Long menuId : listMenu) {
                RemoveFunctionMenuDto removeFunctionMenuDto = new RemoveFunctionMenuDto();
                removeFunctionMenuDto.setMenuId(menuId);
                List<Long> listFunctions = functionResponseDtoList.stream()
                        .filter(x -> Objects.equals(x.getMenuId(), menuId)).map(FunctionResponseDto::getId)
                        .collect(Collectors.toList());
                removeFunctionMenuDto.setFunctionIds(listFunctions);
                removeFunctionMenuDtoList.add(removeFunctionMenuDto);
            }
        }
        if (!CollectionUtils.isEmpty(removeFunctionMenuDtoList)) {
            for (RemoveFunctionMenuDto removeFunctionMenuDto : removeFunctionMenuDtoList) {
                Menu menu = menuRepository.findById(removeFunctionMenuDto.getMenuId()).orElse(null);
                if (menu == null || menu.getIsDeleted() == 1) {
                    throw new BusinessException(BusinessExceptionCode.MENU_NOT_FOUND,
                            messageCommon.getMessage(BusinessExceptionCode.MENU_NOT_FOUND));
                }
                menuRepository.removeFunction(removeFunctionMenuDto.getFunctionIds(), removeFunctionMenuDto.getMenuId());
                responseDto.setIsSuccess(true);
            }
        }
        return responseDto;
    }

    @Override
    public Boolean addFunctionMenu(AddFunctionMenuDto addFunctionMenuDto) {
        Menu menu = menuRepository.findById(addFunctionMenuDto.getMenuId()).orElse(null);
        if (menu == null || menu.getIsDeleted() == 1) {
            throw new BusinessException(BusinessExceptionCode.MENU_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.MENU_NOT_FOUND));
        }
        if (!CollectionUtils.isEmpty(addFunctionMenuDto.getFunctionIds()) && !CollectionUtils.isEmpty(menu.getFunctions())) {
            menu.getFunctions().removeIf(x -> addFunctionMenuDto.getFunctionIds().contains(x.getId()));
        }
        List<Function> newFunctions = functionRepository.findByIds(addFunctionMenuDto.getFunctionIds());
        menu.getFunctions().addAll(newFunctions);
        menuRepository.save(menu);
        return true;
    }
}