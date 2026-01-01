package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import org.aspectj.weaver.loadtime.Agent;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agent_skills")
@Data
public class AgentSkill extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private AiAgent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
