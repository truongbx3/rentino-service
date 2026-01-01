package com.viettel.vss.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {
    private Long id;
    private String menuName;
    private String url;
    private String formName;
    private String description;
    private Boolean isShow;
    private Integer indexOrder;
    private Long moduleId;
    private String moduleName;
    private String icon;
}
