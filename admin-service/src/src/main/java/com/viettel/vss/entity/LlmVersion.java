package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "llm-version")
public class LlmVersion extends BaseEntity {

	@Basic
	@Column(name = "code")
	private String code;

	@Basic
	@Column(name = "name")
	private String name;

    @Basic
    @Column(name = "version")
    private String version;

	@Basic
	@Column(name = "description")
	private String description;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;
}
