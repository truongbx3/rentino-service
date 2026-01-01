package com.viettel.vss.dto.action;

import lombok.Data;

import java.util.List;

@Data
public class ActionDetail {
    private String key;
    private String name;
    private String description;
    private List<ActionFunctionDetail> actionFunctionList;
    private List<ActionSkillDetail> actionSkillList;
}
