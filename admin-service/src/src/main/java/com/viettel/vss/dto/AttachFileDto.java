package com.viettel.vss.dto;

import java.util.Date;

import com.viettel.vss.base.BaseDto;

import lombok.Data;

@Data
public class AttachFileDto extends BaseDto{
    private Long id;
	private Date createdDate;
	private Date updatedDate;
    private String createdBy;
    private String updatedBy;
    private String fileName;
    private String pathName;
    private String system;
}
