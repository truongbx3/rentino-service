package com.viettel.vss.entity;

import com.viettel.vss.enums.AuditAction;
import lombok.Data;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_username")
    private String actorUsername;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private AuditAction action;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Column(name = "created_at")
    private Instant createdAt;

}
