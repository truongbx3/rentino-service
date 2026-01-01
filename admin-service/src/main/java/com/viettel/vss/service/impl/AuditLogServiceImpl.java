package com.viettel.vss.service.impl;

import com.viettel.vss.config.bean.UserCustom;
import com.viettel.vss.entity.AuditLog;
import com.viettel.vss.enums.AuditAction;
import com.viettel.vss.repository.AuditLogRepository;
import com.viettel.vss.config.sercurity.MyCustomUserDetails;
import com.viettel.vss.service.AuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.time.Instant;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }


    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "System";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof MyCustomUserDetails) {
            MyCustomUserDetails userDetails = (MyCustomUserDetails) principal;
            return userDetails.getUsername();
        } else if (principal instanceof UserCustom) {
            UserCustom userCustom = (UserCustom) principal;
            return userCustom.getUsername();
        } else if (principal instanceof String) {
            String username = (String) principal;
            return username;
        }
        return "System";
    }

    /**
     * Hauvt4
     * Ghi log thao tác
     *
     * @param targetType Loại đối tượng (ví dụ: "Skill", "AiAgent")
     * @param targetId   ID đối tượng bị tác động
     * @param action     Hành động (CREATE, UPDATE, DELETE, ASSIGN)
     * @param payload    JSON chi tiết trước/sau
     */
    @Transactional
    public void log(String targetType, String targetId, AuditAction action, String payload) {
        AuditLog log = new AuditLog();
        log.setActorUsername(getCurrentUsername());
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setAction(action); // enum sẽ được map sang string
        log.setPayload(payload);
        log.setCreatedAt(Instant.now());

        auditLogRepository.save(log);
    }

}
