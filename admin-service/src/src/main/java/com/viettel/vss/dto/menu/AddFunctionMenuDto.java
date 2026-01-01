package com.viettel.vss.dto.menu;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddFunctionMenuDto {
    @NotNull
    private Long menuId;

    private List<Long> functionIds;
}
