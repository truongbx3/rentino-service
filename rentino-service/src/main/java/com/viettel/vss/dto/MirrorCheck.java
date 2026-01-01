package com.viettel.vss.dto;

import com.viettel.vss.dto.attach_file.FunctionItem;
import com.viettel.vss.dto.attach_file.ImageItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MirrorCheck {
    @NotNull
    public ImageItem item;
    @NotNull
    public String value;
}
