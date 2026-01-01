package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.constant.BusinessExceptionCode;
import com.viettel.vss.dto.system.FilterSystemRequest;
import com.viettel.vss.dto.system.SystemDto;
import com.viettel.vss.dto.system.SystemResponseDto;
import com.viettel.vss.entity.AuthType;
import com.viettel.vss.entity.SystemAuth;
import com.viettel.vss.entity.System;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.AuthTypeRepository;
import com.viettel.vss.repository.SystemAuthRepository;
import com.viettel.vss.repository.SystemRepository;
import com.viettel.vss.service.SystemService;
import com.viettel.vss.util.MessageCommon;
import com.viettel.vss.util.ValidationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SystemServiceImpl extends BaseServiceImpl<System, SystemDto> implements SystemService {

    @Autowired
    private SystemAuthRepository systemAuthRepository;

    @Autowired
    private AuthTypeRepository authTypeRepository;

    @Autowired
    private MessageCommon messageCommon;

    private final SystemRepository systemRepository;

    public SystemServiceImpl(SystemRepository systemRepository) {
        super(systemRepository, System.class, SystemDto.class);
        this.systemRepository = systemRepository;
    }

    @Override
    @Transactional
    public System saveSystem(SystemDto data) {
        if (ObjectUtils.isEmpty(data.getName())) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_EMPTY_NAME,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_EMPTY_NAME));
        }

        if (ObjectUtils.isEmpty(data.getBaseUrl())) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_EMPTY_BASE_URL,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_EMPTY_BASE_URL));
        }

        if (!ValidationUtils.isValidUrl(data.getBaseUrl())) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_INVALID_BASE_URL,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_INVALID_BASE_URL));
        }

        Boolean existedSystemName = systemRepository.findByNameAndIsDeleted(data.getName(), data.getId());
        if (Boolean.TRUE.equals(existedSystemName)) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_EXISTED_NAME,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_EXISTED_NAME));
        }

        Boolean existedSystemBaseURL = systemRepository.findByBaseUrlAndIsDeleted(data.getBaseUrl(), data.getId());
        if (Boolean.TRUE.equals(existedSystemBaseURL)) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_EXISTED_BASE_URL,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_EXISTED_BASE_URL));
        }

        System system = new System();
        SystemAuth systemAuth = new SystemAuth();
        if (!ObjectUtils.isEmpty(data.getSystemAuth()) && !ObjectUtils.isEmpty(data.getSystemAuth().getAuthType())) {
            Boolean existedAuthType = authTypeRepository.existsByCodeAndIsDeleted(data.getSystemAuth().getAuthType(), 0);
            if (Boolean.FALSE.equals(existedAuthType)) {
                throw new BusinessException(BusinessExceptionCode.AUTH_TYPE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.AUTH_TYPE_NOT_FOUND));

            }
        }

        system.setName(data.getName());
        system.setBaseUrl(data.getBaseUrl());
        system.setDescription(data.getDescription());
        system.setIsDeleted(data.getIsDeleted());
        system.setIsActive(data.getIsActive());

        if (!ObjectUtils.isEmpty(data.getId())) {
            System systemEntity = systemRepository.findByIdAndIsDeleted(data.getId(), 0);
            if (ObjectUtils.isEmpty(systemEntity)) {
                throw new BusinessException(BusinessExceptionCode.SYSTEM_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.SYSTEM_NOT_FOUND));
            }

            system.setId(systemEntity.getId());
            system.setCreatedBy(systemEntity.getCreatedBy());
            system.setCreatedDate(systemEntity.getCreatedDate());

            SystemAuth systemAuthEntity = systemAuthRepository.findBySystemIdAndIsDeleted(systemEntity.getId(), 0);
            if (!ObjectUtils.isEmpty(systemAuthEntity)) {
                systemAuth.setId(systemAuthEntity.getId());
                systemAuth.setCreatedBy(systemAuthEntity.getCreatedBy());
                systemAuth.setCreatedDate(systemAuthEntity.getCreatedDate());
            }

        }

        System savedSystem = systemRepository.save(system);
        SystemAuth savedSystemAuth = null;
        if (!ObjectUtils.isEmpty(data.getSystemAuth())) {
            systemAuth.setCredential(data.getSystemAuth().getCredential());
            systemAuth.setFetchDuration(data.getSystemAuth().getFetchDuration());
            systemAuth.setAuthType(data.getSystemAuth().getAuthType());
            systemAuth.setSystem(savedSystem);
            savedSystemAuth = systemAuthRepository.save(systemAuth);
        }

        savedSystem.setSystemAuth(savedSystemAuth);
        return savedSystem;
    }

    @Override
    public void deleteSystem(List<Long> ids) {
        List<System> data = systemRepository.findAllByIdInAndIsDeleted(ids, 0);
        if (ObjectUtils.isEmpty(data)) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_NOT_FOUND));
        }

        //TODO: Check đang sử dụng

        super.deleteByIds(ids);
    }

    @Override
    public List<SystemResponseDto> getSystemById(List<Long> ids) {
        List<SystemResponseDto> result = systemRepository.getSystemById(ids, 0);
        if (ObjectUtils.isEmpty(result)) {
            throw new BusinessException(BusinessExceptionCode.SYSTEM_NOT_FOUND,
                    messageCommon.getMessage(BusinessExceptionCode.SYSTEM_NOT_FOUND));
        }

        for (SystemResponseDto dto : result) {
            AuthType authType = authTypeRepository.findByCodeAndIsDeleted(dto.getAuthType(), 0);
            if (ObjectUtils.isEmpty(authType)) {
                throw new BusinessException(BusinessExceptionCode.AUTH_TYPE_NOT_FOUND,
                        messageCommon.getMessage(BusinessExceptionCode.AUTH_TYPE_NOT_FOUND));
            }
            dto.setAuthTypeName(authType.getName());
        }

        return result;
    }

    @Override
    public Page<SystemDto> getSystems(FilterSystemRequest filterSystemRequest) {
        Sort.Direction direction = Sort.Direction.fromString(filterSystemRequest.getSortDirection());
        Pageable pageable = PageRequest.of(filterSystemRequest.getPage(),
                filterSystemRequest.getSize(),
                Sort.by(direction, filterSystemRequest.getSortBy()));
        return systemRepository.getSystems(filterSystemRequest, pageable);
    }
}