package com.viettel.vss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "role_group")
public class RoleGroup extends BaseEntity {

	@Basic
	@Column(name = "default_group")
	private Boolean defaultGroup;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "is_admin")
    private Boolean isAdmin = false;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @Basic
    @Column(name = "role_group_code")
    private String roleGroupCode;

    @Basic
    @Column(name = "role_group_name")
    private String roleGroupName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "function_role_group",
            joinColumns = @JoinColumn(name = "role_group_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    @JsonIgnore
    private List<Function> functions;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "roleGroups")
    @JsonIgnore
    private List<UserEntity> users;
}
