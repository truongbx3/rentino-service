package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "execution_type")
public class ExecutionType extends BaseEntity {

	@Basic
	@Column(name = "code")
	private String code;

	@Basic
	@Column(name = "name")
	private String name;

	@Basic
	@Column(name = "description")
	private String description;

    @Basic
    @Column(name = "meta_data", columnDefinition = "TEXT")
    private String metaData;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;
}
