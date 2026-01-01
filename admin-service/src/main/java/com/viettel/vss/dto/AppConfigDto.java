package com.viettel.vss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.vss.base.BaseDto;
import java.util.Date;
import com.viettel.vss.constant.CommonConstants;
import lombok.Data;

@Data
public class AppConfigDto extends BaseDto{
	private Long id;
	private String username;
	private String configCode;
	private String configName;
	private String description;
	private String value;
	private Date createdTime;
	private String createdBy;
	private Date updatedTime;
	private String updatedBy;
	@JsonProperty(value = "isDeleted")
	private int isDeleted;
	@JsonProperty(value = "isActive")
	private int isActive;
	@JsonProperty(value = "isPassword")
	private int isPassword;
	private int indexOrder;
}