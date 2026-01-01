package com.viettel.vss.entity;

import com.viettel.vss.base.BaseEntity;
import lombok.Data;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;

@Entity
@Data
@Table(name = "app_config")
public class AppConfig extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Basic
	@CreatedBy
	@Column(name = "username")
	private String username;

	@Basic
	@Column(name = "config_code")
	private String configCode;

	@Basic
	@Column(name = "config_name")
	private String configName;

	@Basic
	@Column(name = "description")
	private String description;

	@Basic
	@Column(name = "value")
	private String value;


	@Basic
	@Column(name = "is_password")
	private int isPassword;

	@Basic
	@Column(name = "index_order")
	private int indexOrder;
	@Basic
	@Column(name = "is_active")
	private int isActive;
}
