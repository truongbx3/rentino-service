package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "skill_action")
public class SkillActionEntity extends BaseEntity {

    // Many-to-One tới Skill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false, insertable = false, updatable = false)
    private Skill skill;

    // Many-to-One tới Action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id", nullable = false, insertable = false, updatable = false)
    private ActionEntity action;

    @Basic
    @Column(name = "skill_id")
    private Long skillId;

    @Basic
    @Column(name = "action_id")
    private Long actionId;

}
