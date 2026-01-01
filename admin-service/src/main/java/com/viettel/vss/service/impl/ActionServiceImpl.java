package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.ActionDto;
import com.viettel.vss.dto.action.*;
import com.viettel.vss.dto.support.DateSupport;
import com.viettel.vss.entity.ActionEntity;
import com.viettel.vss.exception.BusinessException;
import com.viettel.vss.repository.ActionRepository;
import com.viettel.vss.service.ActionService;
import com.viettel.vss.service.FunctionActionService;
import com.viettel.vss.service.SkillActionService;
import com.viettel.vss.util.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ActionServiceImpl extends BaseServiceImpl<ActionEntity, ActionDto> implements ActionService {
    private final ActionRepository repository;
    private final FunctionActionService functionActionService;
    private final SkillActionService skillActionService;

    public ActionServiceImpl(ActionRepository repository, FunctionActionService functionActionService, SkillActionService skillActionService){
        super(repository, ActionEntity.class, ActionDto.class);
        this.repository = repository;
        this.functionActionService = functionActionService;
        this.skillActionService = skillActionService;
    }

    @Override
    @Transactional
    public ActionDto saveObjectCus(ActionCreateRequest request) {
        if(ObjectUtils.isEmpty(request.getKey())){
            throw new BusinessException("Key action không được để trống");
        }
        if(ObjectUtils.isEmpty(request.getName())){
            throw new BusinessException("Tên action không được để trống");
        }
        if(ObjectUtils.isEmpty(request.getId())){
            ActionDto toSave = ObjectMapperUtils.map(request, ActionDto.class);
            ActionEntity duplicate = repository.checkDuplicate(toSave);
            if(!ObjectUtils.isEmpty(duplicate)){
                throw new BusinessException("Key , Tên hành động đã tồn tại");
            }
            ActionDto saved = super.saveObject(toSave);
            functionActionService.saveActionFunctionList(saved.getId(), request.getFunctionList());
            skillActionService.saveSkillActionList(request.getSkillIds(), saved.getId());
            return saved;
        }else{
            ActionDto toSave = ObjectMapperUtils.map(request, ActionDto.class);
            ActionEntity duplicate = repository.checkDuplicate(toSave);
            if(!ObjectUtils.isEmpty(duplicate)){
                throw new BusinessException("Key , Tên hành động đã tồn tại");
            }
            ActionDto saved = super.saveObject(toSave);
            if(!ObjectUtils.isEmpty(request.getIsChangeSkills()) && request.getIsChangeSkills()){
                skillActionService.saveSkillActionList(request.getSkillIds(), saved.getId());
            }
            if(!ObjectUtils.isEmpty(request.getIsChangeFunctions()) && request.getIsChangeFunctions()){
                functionActionService.saveActionFunctionList(saved.getId(), request.getFunctionList());
            }
            return saved;
        }
    }

    @Override
    public ActionDetail getDetailAction(Long id) {
        ActionEntity saved = repository.findById(id).orElseThrow(() -> new BusinessException(""));
        ActionDetail detail = ObjectMapperUtils.map(saved, ActionDetail.class);
        List<ActionFunctionDetail> actionFunctionList = functionActionService.getActionFunctionDetail(id);
        detail.setActionFunctionList(actionFunctionList);
        List<ActionSkillDetail> actionSkillList = skillActionService.getSkillActionDetail(id);
        detail.setActionSkillList(actionSkillList);
        return detail;
    }

    @Override
    public Page<ActionListDto> filterAction (FilterActionRequest request, Integer size, Integer page, String sortBy, String sortDirection){
        if(ObjectUtils.isEmpty(request.getCreatedDate())){
            request.setCreatedDate(new DateSupport());
        }
        if(ObjectUtils.isEmpty(request.getUpdatedDate())){
            request.setUpdatedDate(new DateSupport());
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ActionListDto> savedData = repository.filterAction(request, pageable);
        if(!ObjectUtils.isEmpty(savedData)){
            List<Long> actionIds = savedData.map(ActionListDto::getId).getContent();
            Map<Long, List<ActionSkillDetail>> actionSkillMap = skillActionService.getSkillActionDetailMap(actionIds);
            savedData.forEach(data -> {
                if(actionSkillMap.containsKey(data.getId())){
                    data.setActionSkillDetailList(actionSkillMap.get(data.getId()));
                }
            });
        }
        return savedData;
    }
}
