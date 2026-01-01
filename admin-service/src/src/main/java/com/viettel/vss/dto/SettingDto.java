package com.viettel.vss.dto;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;

import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.vss.base.BaseDto;

import lombok.Data;

@Data
public class SettingDto extends BaseDto{
    private Long id;


	private String username;


	private String tableName;


	
	private int status;

	
	private String config;

    private Date createdDate;
	private String createBy;
	@JsonProperty(value = "isDeleted")
	private int isDeleted;
	private Date updatedDate;
	private String updatedBy;
}
