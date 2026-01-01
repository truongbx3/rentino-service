package com.viettel.vss.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.config.bean.UserCustom;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.module.*;
import com.viettel.vss.entity.Menu;
import com.viettel.vss.entity.Module;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.MenuRepository;
import com.viettel.vss.repository.ModuleRepository;
import com.viettel.vss.service.ModuleService;
import com.viettel.vss.util.MenuUtils;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.ValidationUtils;
import org.springframework.util.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ModuleServiceImpl extends BaseServiceImpl<Module, ModuleDto> implements ModuleService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MessageCommon messageCommon;

    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        super(moduleRepository, Module.class, ModuleDto.class);
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<ModuleOrMenuResponseDto> getListModule(String locale) {
        List<Module> modules = moduleRepository.findAll();
        List<ModuleOrMenuResponseDto> originModuleList =
                modules.stream().map(x -> MenuUtils.mapFromModule(x, locale)).collect(Collectors.toList());
        List<ModuleOrMenuResponseDto> moduleResponseDtoList =
                originModuleList.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(moduleResponseDtoList)) {
            for (ModuleOrMenuResponseDto moduleResponseDto : moduleResponseDtoList) {
                MenuUtils.getModuleChildListUnMenu(originModuleList, moduleResponseDto);
            }
        }
        return moduleResponseDtoList;
    }

    @Override
    public Long saveModuleMenu(ModuleDto moduleDto) {
        if (!ObjectUtils.isEmpty(moduleDto.getIcon()) && !ValidationUtils.isValidSvgUrl(moduleDto.getIcon())) {
            throw new BusinessException(BusinessExceptionCode.MODULE_INVALID_ICON,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_INVALID_ICON));
        }

        if (moduleDto.getType() == 1) {
            Module module = new Module();
            if (moduleRepository.findModuleByModuleName(moduleDto.getName().toUpperCase())
                    .stream().anyMatch(name -> name.equalsIgnoreCase(moduleDto.getName()))) {
                throw new BusinessException(BusinessExceptionCode.MODULE_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.MODULE_EXISTS, moduleDto.getName()));
            }
            module.setModuleName(moduleDto.getName());
            module.setDescription(moduleDto.getDescription());
            module.setUrl(moduleDto.getUrl());
            module.setIsShow(moduleDto.getIsShow());
            module.setFormName(moduleDto.getFormName());
            module.setIndexOrder(moduleDto.getIndexOrder());
            module.setIcon(moduleDto.getIcon());
            module.setParentId(moduleDto.getParentId());
            return moduleRepository.save(module).getId();
        } else {
            if (menuRepository.checkExistMenuName(moduleDto.getName().toUpperCase(), moduleDto.getModuleId()).stream()
                    .anyMatch(menuName -> menuName.equalsIgnoreCase(moduleDto.getName()))) {
                throw new BusinessException(BusinessExceptionCode.MENU_EXISTS,
                        messageCommon.getMessage(BusinessExceptionCode.MENU_EXISTS, moduleDto.getName()));
            }
            Menu menu = new Menu();
            menu.setMenuName(moduleDto.getName());
            menu.setDescription(moduleDto.getDescription());
            menu.setFormName(moduleDto.getFormName());
            menu.setUrl(moduleDto.getUrl());
            menu.setIsShow(moduleDto.getIsShow());
            menu.setFormName(moduleDto.getFormName());
            menu.setIndexOrder(moduleDto.getIndexOrder());
            menu.setIcon(moduleDto.getIcon());
            if (moduleDto.getModuleId() != null) {
                Module module = new Module();
                module.setId(moduleDto.getModuleId());
                menu.setModule(module);
            }
            return menuRepository.save(menu).getId();
        }
    }

    @Override
    public Boolean deleteModule(Long id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null || module.getIsDeleted() == 1) { //|| !Boolean.TRUE.equals(module.getIsActive())
            throw new BusinessException(BusinessExceptionCode.MODULE_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_NOT_FOUND));
        }

        if (menuRepository.countFunctionOfMenu(id) != 0) {
            throw new BusinessException(BusinessExceptionCode.MODULE_HAS_MENU,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_HAS_MENU));
        }

        module.setIsDeleted(1);
        moduleRepository.save(module);
        return true;
    }

    @Override
    public DeleteModuleResponseDto deleteListModule(List<Long> ids) {
        DeleteModuleResponseDto responseDto = new DeleteModuleResponseDto();
        List<String> error = new ArrayList<>();
        boolean isContinue;
        for (Long id : ids) {
            isContinue = false;
            Module module = moduleRepository.findById(id).orElse(null);
            if (module == null || module.getIsDeleted() == 1) {
                error.add(messageCommon.getMessage(BusinessExceptionCode.MODULE_NOT_FOUND));
                isContinue = true;
            }
            if (!menuRepository.getMenuByModuleId(id).isEmpty()) {
                error.add(messageCommon.getMessage(BusinessExceptionCode.MODULE_HAS_MENU));
                isContinue = true;
            }
            if (isContinue) continue;
            module.setIsDeleted(1);
            moduleRepository.save(module);
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
    public Long updateModule(ModuleRequestDto moduleRequestDto) {
        if (!ObjectUtils.isEmpty(moduleRequestDto.getIcon()) && !ValidationUtils.isValidSvgUrl(moduleRequestDto.getIcon())) {
            throw new BusinessException(BusinessExceptionCode.MODULE_INVALID_ICON,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_INVALID_ICON));
        }

        Module module = moduleRepository.findById(moduleRequestDto.getId()).orElse(null);
        if (module == null) {
            throw new BusinessException(BusinessExceptionCode.MODULE_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_NOT_FOUND));
        } else if (moduleRequestDto.getModuleName().equalsIgnoreCase(module.getModuleName())) {
            modelMapper.map(moduleRequestDto, module);
            moduleRepository.save(module);
            return module.getId();
        } else if (moduleRepository.findModuleByModuleName(moduleRequestDto.getModuleName().toUpperCase()).stream()
                .anyMatch(name -> name.equalsIgnoreCase(moduleRequestDto.getModuleName()))) {
            throw new BusinessException(BusinessExceptionCode.MODULE_EXISTS,
                    messageCommon.getMessage(BusinessExceptionCode.MODULE_EXISTS, moduleRequestDto.getModuleName()));
        }
        modelMapper.map(moduleRequestDto, module);
        moduleRepository.save(module);

        return module.getId();
    }

    @Override
    public ModuleRequestDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module != null) {
            return modelMapper.map(module, ModuleRequestDto.class);
        }
        return null;
    }

    @Override
    public List<ModuleOrMenuResponseDto> getAllMenu(String locale) {
        List<Module> modules = moduleRepository.findAll();
        List<ModuleOrMenuResponseDto> moduleResponseDtoList = new ArrayList<>();

        List<ModuleOrMenuResponseDto> originModuleList =
                modules.stream().map(x -> MenuUtils.mapFromModule(x, locale)).collect(Collectors.toList());
        List<ModuleOrMenuResponseDto> moduleResponseDtoList1 =
                originModuleList.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList());

        List<Menu> menus = menuRepository.getAll();
        List<ModuleOrMenuResponseDto> originMenuList = menus.stream()
                .map(x -> MenuUtils.mapFromMenu(x, modelMapper, locale))
                .collect(Collectors.toList());
        List<ModuleOrMenuResponseDto> menuNoModule = originMenuList.stream()
                .filter(menu -> menu.getModuleId() == null)
                .collect(Collectors.toList());

        for (ModuleOrMenuResponseDto module : moduleResponseDtoList1) {
            MenuUtils.getModuleAllChildList(originModuleList, module, originMenuList);
        }
        moduleResponseDtoList.addAll(moduleResponseDtoList1);
        moduleResponseDtoList.addAll(menuNoModule);

        return moduleResponseDtoList;
    }

    @Override
    public List<ModuleOrMenuResponseAlwaysVisibleDto> getListMenuShow(HttpServletRequest httpServletRequest, String locale) {
        List<ModuleOrMenuResponseDto> moduleResponseDtoList = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().contains("ROLE_ADMIN_SYSTEM"));
        String email = null;
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserCustom) {
                UserCustom currentUserCustom = (UserCustom) principal;
                String token = currentUserCustom.getToken();

                if (!ObjectUtils.isEmpty(token)) {
                    token = token.replace("Bearer ", "");
                    DecodedJWT jwt = JWT.decode(token);
                    email = jwt.getSubject();
                }
            }
        }

        if (ObjectUtils.isEmpty(email)) {
            throw new BusinessException(BusinessExceptionCode.USER_NOT_EXISTED,
                    messageCommon.getMessage(BusinessExceptionCode.USER_NOT_EXISTED));
        }

        List<Menu> menus = menuRepository.getMenuUser(isAdmin, email);

        //all menu show
        List<ModuleOrMenuResponseDto> originMenuList = menus.stream()
                .filter(menu -> Boolean.TRUE.equals(menu.getIsShow()))
                .map(x -> MenuUtils.mapFromMenu(x, modelMapper, locale))
                .collect(Collectors.toList());

        //menu no module
        List<ModuleOrMenuResponseDto> menuNoModule = originMenuList.stream()
                .filter(menu -> menu.getModuleId() == null)
                .collect(Collectors.toList());
        originMenuList.removeAll(menuNoModule);

        List<Long> allModuleId = originMenuList.stream().map(ModuleOrMenuResponseDto::getModuleId)
                .distinct().collect(Collectors.toList());

        //all module
        List<Module> modules = moduleRepository.findByModuleId(allModuleId);
        List<ModuleOrMenuResponseDto> originModuleList = modules.stream()
                .map(m -> MenuUtils.mapFromModule(m, locale)).collect(Collectors.toList());

        //module chinh
        List<ModuleOrMenuResponseDto> listModuleMain = modules.stream().filter(x -> x.getParentId() == null)
                .map(m -> MenuUtils.mapFromModule(m, locale)).collect(Collectors.toList());
        originModuleList.removeAll(listModuleMain);

        for (ModuleOrMenuResponseDto module : listModuleMain) {
            MenuUtils.getModuleAllChildList(originModuleList, module, originMenuList);
        }
        listModuleMain = listModuleMain.stream()
                .filter(x -> !CollectionUtils.isEmpty(x.getItems()))
                .distinct().collect(Collectors.toList());
        moduleResponseDtoList.addAll(menuNoModule);
        moduleResponseDtoList.addAll(listModuleMain);

        return moduleResponseDtoList.stream().sorted(Comparator.comparing(ModuleOrMenuResponseDto::getIndexOrder, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(x -> modelMapper.map(x, ModuleOrMenuResponseAlwaysVisibleDto.class))
                .collect(Collectors.toList());
    }
}