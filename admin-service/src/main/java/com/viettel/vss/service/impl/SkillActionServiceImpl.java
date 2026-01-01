package com.viettel.vss.service.impl;

import com.viettel.vss.base.BaseServiceImpl;
import com.viettel.vss.dto.SkillActionDto;
import com.viettel.vss.dto.action.ActionSkillDetail;
import com.viettel.vss.dto.action.ActionSkillDetailProjection;
import com.viettel.vss.entity.SkillActionEntity;
import com.viettel.vss.repository.SkillActionRepository;
import com.viettel.vss.service.SkillActionService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkillActionServiceImpl extends BaseServiceImpl<SkillActionEntity, SkillActionDto> implements SkillActionService {

    private final SkillActionRepository skillActionRepository;

    public SkillActionServiceImpl(SkillActionRepository skillActionRepository) {
        super(skillActionRepository, SkillActionEntity.class, SkillActionDto.class);
        this.skillActionRepository = skillActionRepository;
    }


    @Override
    public void saveSkillActionList(List<Long> skillIds, Long actionId) {
        this.deleteByActionId(actionId);
        List<SkillActionDto> toSave = skillIds.stream().map(skillId -> {
            SkillActionDto dto = new SkillActionDto();
            dto.setSkillId(skillId);
            dto.setActionId(actionId);
            return dto;
            }).collect(Collectors.toList());
        super.saveListObject(toSave);
    }

    @Override
    public void deleteByActionId(Long actionId) {
        List<SkillActionEntity> savedEntities = skillActionRepository.findByActionId(actionId);
        if(!ObjectUtils.isEmpty(savedEntities)){
            savedEntities.forEach(skillActionEntity -> {
                skillActionEntity.setIsDeleted(0);
            });
            skillActionRepository.saveAll(savedEntities);
        }
    }

    @Override
    public List<ActionSkillDetail> getSkillActionDetail(Long actionId) {
        return skillActionRepository.getActionSkillDetail(actionId);
    }

    @Override
    public Map<Long, List<ActionSkillDetail>> getSkillActionDetailMap(List<Long> actionIds) {
        List<ActionSkillDetailProjection> details = skillActionRepository.getActionSkillDetailByActionIds(actionIds);
        if(!ObjectUtils.isEmpty(details)){
            return details.stream().collect(Collectors.groupingBy(
                    ActionSkillDetailProjection::getActionId,
                    Collectors.mapping(
                            detail -> {
                                ActionSkillDetail dto = new ActionSkillDetail();
                                dto.setSkillId(detail.getSkillId());
                                dto.setSkillName(detail.getSkillName());
                                return dto;
                            },
                            Collectors.toList()
                    )
            ));
        }
        return Map.of();
    }
}
