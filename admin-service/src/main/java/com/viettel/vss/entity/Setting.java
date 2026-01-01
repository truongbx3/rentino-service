package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Data
@Table(name = "setting")
public class Setting extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Basic
	@CreatedBy
	@Column(name = "username")
	private String username;

	@Basic
	@Column(name = "table_name")
	private String tableName;


	@Basic
	@Column(name = "status")
	private int status;

	@Basic
	@Column(name = "config")
	private String config;
}
