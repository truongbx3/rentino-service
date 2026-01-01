package com.viettel.vss.dto.action;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionFunctionDetail {
    private Long functionId;
    private String functionName;
    private String functionKeyCode;
    private Integer indexOrder;
}
