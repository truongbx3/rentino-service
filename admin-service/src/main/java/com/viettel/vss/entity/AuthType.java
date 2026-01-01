package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "auth_types")
public class AuthType extends BaseEntity {

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
    @Column(name = "is_active")
    private int isActive = 1;
}
