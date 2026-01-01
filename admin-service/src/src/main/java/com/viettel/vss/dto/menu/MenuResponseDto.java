package com.viettel.vss.dto.menu;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuResponseDto {
    private Long id;
    private String menuName;
    private String url;
    private String description;
    private Integer indexOrder;
    private String icon;
    private Boolean isShow;
    private Long moduleId;
}
