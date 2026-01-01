package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.ActionDto;
import com.viettel.vss.dto.action.ActionCreateRequest;
import com.viettel.vss.dto.action.ActionDetail;
import com.viettel.vss.dto.action.ActionListDto;
import com.viettel.vss.dto.action.FilterActionRequest;
import com.viettel.vss.entity.ActionEntity;
import org.springframework.data.domain.Page;

public interface ActionService extends BaseService<ActionEntity, ActionDto> {
    ActionDto saveObjectCus(ActionCreateRequest request);
    ActionDetail getDetailAction(Long id);
    Page<ActionListDto> filterAction(FilterActionRequest request, Integer size, Integer page, String sortBy, String sortDirection);
}
