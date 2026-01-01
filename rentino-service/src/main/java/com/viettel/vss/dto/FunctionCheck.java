package com.viettel.vss.dto;

import com.viettel.vss.dto.attach_file.FunctionItem;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FunctionCheck {
    @NotNull
    public FunctionItem item;
    @NotNull
    public Boolean value;
}
