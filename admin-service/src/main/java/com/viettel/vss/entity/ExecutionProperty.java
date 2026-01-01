package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "execution_properties")
public class ExecutionProperty extends BaseEntity {

	@Basic
	@Column(name = "parent_property_id")
	private Long parentPropertyId;

	@Basic
	@Column(name = "property_name")
	private String propertyName;

	@Basic
	@Column(name = "data_type")
	private String dataType;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "required")
    private int required = 1;

    @Basic
    @Column(name = "default_value")
    private String defaultValue;

    @Basic
    @Column(name = "index_order")
    private Integer indexOrder;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @ManyToOne
    @JoinColumn(name = "execution_type_id", referencedColumnName = "id")
    private ExecutionType executionType;
}
