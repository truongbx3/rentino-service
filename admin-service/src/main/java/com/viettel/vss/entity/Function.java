package com.viettel.vss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "function")
public class Function extends BaseEntity {

	@Basic
	@Column(name = "function_name")
	private String functionName;

	@Basic
	@Column(name = "function_code")
	private String functionCode;

	@Basic
	@Column(name = "description")
	private String description;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @ManyToMany(mappedBy = "functions")
    @JsonIgnore
    private List<Menu> menus;

    @ManyToMany(mappedBy = "functions")
    @JsonIgnore
    private List<RoleGroup> roleGroupEntities;
}
