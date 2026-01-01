package com.viettel.vss.dto.menu;

import lombok.Data;

import java.util.List;

@Data
public class DeleteMenuResponseDto {
    private Boolean isSuccess;
    private List<String> error;
}
