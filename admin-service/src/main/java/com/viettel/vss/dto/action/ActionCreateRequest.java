package com.viettel.vss.dto.action;

import lombok.Data;

import java.util.List;

@Data
public class ActionCreateRequest {
    private Long id;
    private String key;
    private String name;
    private String description;
    private List<ActionFunctionCreateRequest> functionList;
    private List<Long> skillIds;
    private Boolean isChangeSkills;
    private Boolean isChangeFunctions;
}
