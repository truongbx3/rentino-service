package com.viettel.vss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Company {
    VTIT("VIETTEL SOFTWARE", "VTIT"),;

    private final String name;
    private final String code;
}
