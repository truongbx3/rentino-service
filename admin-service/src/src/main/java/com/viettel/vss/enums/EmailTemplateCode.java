package com.viettel.vss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateCode {
    ACTIVATE_ACCOUNT("AI_AGENT_1");
    public final String templateCode;
}
