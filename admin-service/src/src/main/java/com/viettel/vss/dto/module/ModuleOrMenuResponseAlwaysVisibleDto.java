package com.viettel.vss.dto.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleOrMenuResponseAlwaysVisibleDto {
    private Long id;
    private Long moduleId;
    private String moduleName;
    private String description;
    private String icon;
    private Long parentId;
    private String menuName;
    private String url;
    private Integer indexOrder;
    private String applicationCode;
    private List<ModuleOrMenuResponseAlwaysVisibleDto> items;
}
