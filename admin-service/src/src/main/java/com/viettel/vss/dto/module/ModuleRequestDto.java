package com.viettel.vss.dto.module;

import lombok.Data;

@Data
public class ModuleRequestDto {
    private Long id;
    private String moduleName;
    private String description;
    private Boolean isShow;
    private String url;
    private String formName;
    private Integer indexOrder;
    private String icon;
    private Long parentId;
    private String application;
}
