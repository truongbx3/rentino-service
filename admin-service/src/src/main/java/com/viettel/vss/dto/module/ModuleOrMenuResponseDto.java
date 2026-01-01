package com.viettel.vss.dto.module;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ModuleOrMenuResponseDto {
    private Long id;
    private Long moduleId;
    private String moduleName;
    private String moduleCode;
    private String description;
    private Boolean isActive;
    private String icon;
    private Long parentId;
    private String menuName;
    private String url;
    private Integer indexOrder;
    private Boolean isShow;
    private String applicationCode;
    private List<ModuleOrMenuResponseDto> items;
}
