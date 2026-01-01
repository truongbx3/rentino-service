package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "action")
@Data
public class ActionEntity extends BaseEntity {

    @Basic
    @Column(name = "`key`")
    private String key;

    @Basic
    @Column(name = "`name`")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

}
