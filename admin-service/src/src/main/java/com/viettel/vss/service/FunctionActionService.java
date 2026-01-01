package com.viettel.vss.service;

import com.viettel.vss.base.BaseService;
import com.viettel.vss.dto.action.ActionFunctionCreateRequest;
import com.viettel.vss.dto.action.ActionFunctionDetail;
import com.viettel.vss.dto.user.FunctionActionDto;
import com.viettel.vss.entity.FunctionActionEntity;

import java.util.List;
import java.util.Map;

public interface FunctionActionService extends BaseService<FunctionActionEntity, FunctionActionDto> {
    void saveActionFunctionList(Long actionId, List<ActionFunctionCreateRequest> functionList);
    void deleteByActionId(Long actionId);
    List<ActionFunctionDetail> getActionFunctionDetail(Long actionId);
}
