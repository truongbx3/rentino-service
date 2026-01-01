package com.viettel.vss.dto.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.vss.base.BaseDto;
import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuDto extends BaseDto{
	private Long id;
	private String menuName;
	private String menuCode;
	private String url;
	@JsonProperty(value = "isShow")
	private int isShow;
	@NotBlank
	private Long indexOrder;
	private Date createdDate;
	private String createdBy;
	@JsonProperty(value = "isDeleted")
	private int isDeleted;
	private Date updatedDate;
	private String updatedBy;
	private String description;
	private String formName;
	private String icon;
	private Long parentId;
	private List<MenuDto> menuDtos;
}