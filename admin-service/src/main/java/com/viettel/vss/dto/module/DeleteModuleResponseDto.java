package com.viettel.vss.dto.module;

import lombok.Data;

import java.util.List;

@Data
public class DeleteModuleResponseDto {
    private Boolean isSuccess;
    private List<String> error;
}
