package com.viettel.vss.service;

import com.viettel.vss.enums.AuditAction;

public interface AuditLogService {
     void log(String targetType, String targetId, AuditAction action, String payload);
}
