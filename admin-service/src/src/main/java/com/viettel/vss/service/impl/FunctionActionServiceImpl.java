package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.action.ActionFunctionCreateRequest;
import com.viettel.vss.dto.action.ActionFunctionDetail;
import com.viettel.vss.dto.user.FunctionActionDto;
import com.viettel.vss.entity.FunctionActionEntity;
import com.viettel.vss.repository.FunctionActionRepository;
import com.viettel.vss.service.FunctionActionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FunctionActionServiceImpl extends BaseServiceImpl<FunctionActionEntity, FunctionActionDto> implements FunctionActionService {
    private final FunctionActionRepository repository;

    public FunctionActionServiceImpl(FunctionActionRepository repository) {
        super(repository, FunctionActionEntity.class, FunctionActionDto.class);
        this.repository = repository;
    }

    @Override
    @Transactional
    public void saveActionFunctionList(Long actionId, List<ActionFunctionCreateRequest> functionList) {
        this.deleteByActionId(actionId);
        List<FunctionActionDto> toSave = functionList.stream().map(func -> {
            FunctionActionDto dto = new FunctionActionDto();
            dto.setActionId(actionId);
            dto.setFunctionId(func.getFunctionId());
            dto.setOrderIndex(func.getIndexOrder());
            return dto;
        }).collect(Collectors.toList());
        super.saveListObject(toSave);
    }

    @Override
    public void deleteByActionId(Long actionId) {
        List<FunctionActionEntity> savedEntities = repository.findByActionId(actionId);
        if(!ObjectUtils.isEmpty(savedEntities)){
            savedEntities.forEach(functionActionEntity -> {
                functionActionEntity.setIsDeleted(0);
            });
            repository.saveAll(savedEntities);
        }
    }

    @Override
    public List<ActionFunctionDetail> getActionFunctionDetail(Long actionId) {
        return repository.getActionFunctionDetail(actionId);
    }
}
