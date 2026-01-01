package com.viettel.vss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImportStatus {
    SUCCESS("Thành công"),
    FAILED("Thất bại");

    private final String description;
}
