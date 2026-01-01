package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "function_action")
public class FunctionActionEntity extends BaseEntity {

    @Basic
    @Column(name = "function_id")
    private Long functionId;

    @Basic
    @Column(name = "action_id")
    private Long actionId;

    // Many-to-One tới Function
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id", nullable = false, insertable = false, updatable = false)
    private ExecutableFunction function;

    // Many-to-One tới Action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id", nullable = false, insertable = false, updatable = false)
    private ActionEntity action;

    @Basic
    @Column(name = "order_index")
    private Integer orderIndex;
}
