package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "executable_functions")
public class ExecutableFunction extends BaseEntity {

	@Basic
	@Column(name = "key_code")
	private String keyCode;

	@Basic
	@Column(name = "name")
	private String name;

	@Basic
	@Column(name = "data_input_type")
	private String dataInputType;

    @Basic
    @Column(name = "meta_data", columnDefinition = "TEXT")
    private String metaData;

    @Basic
    @Column(name = "is_active")
    private int isActive = 1;

    @ManyToOne
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    private System system;

    @ManyToOne
    @JoinColumn(name = "execution_type_id", referencedColumnName = "id")
    private ExecutionType executionType;
}
