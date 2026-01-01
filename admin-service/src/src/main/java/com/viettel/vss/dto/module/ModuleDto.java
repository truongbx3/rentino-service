package com.viettel.vss.dto.module;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ModuleDto extends BaseDto{
    @NotBlank(message = "Tên Module/Menu không được để trống")
    private String name;
    private String description;
    @NotNull(message = "Loại không được để trống. 1: module")
    private Integer type;
    private Long moduleId;
    private String url;
    private String formName;
    private Boolean isShow;
    private Integer indexOrder;
    private String icon;
    private Long parentId;
}