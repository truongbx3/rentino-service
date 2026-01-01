package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.SkillActionDto;
import com.viettel.vss.dto.action.ActionSkillDetail;
import com.viettel.vss.entity.SkillActionEntity;

import java.util.List;
import java.util.Map;

public interface SkillActionService extends BaseService<SkillActionEntity, SkillActionDto> {
    void saveSkillActionList(List<Long> skillIds, Long actionId);
    void deleteByActionId(Long actionId);
    List<ActionSkillDetail> getSkillActionDetail(Long actionId);
    Map<Long, List<ActionSkillDetail>> getSkillActionDetailMap(List<Long> actionIds);
}
