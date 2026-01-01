package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "module")
public class Module extends BaseEntity {

	@Basic
	@Column(name = "module_name")
	private String moduleName;

	@Basic
	@Column(name = "module_code")
	private String moduleCode;

	@Basic
	@Column(name = "url")
	private String url;

	@Basic
	@Column(name = "is_show")
	private Boolean isShow;

	@Basic
	@Column(name = "index_order")
	private Integer indexOrder;

    @Basic
    @Column(name = "formName")
    private String formName;

	@Basic
	@Column(name = "description")
	private String description;

	@Basic
	@Column(name = "icon")
	private String icon;

	@Basic
	@Column(name = "parent_id")
	private Long parentId;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;
}
