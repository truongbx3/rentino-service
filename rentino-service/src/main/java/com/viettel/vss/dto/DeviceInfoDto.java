package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DeviceInfoDto extends BaseDto{
    private Long id;
	private Date createdDate;
	private Date updatedDate;
    private String type;
    private String deviceName;
    private String deviceCode;
    private String model;
    private String osVersion;
    private String transactionId;
    private String totalRam;
    private String storage;
    private Long userId;
    private BigDecimal price;
    private String frontCheck;
    private String backCheck;
    private String summary;
    private String functionCheck;
    private String finalSummary ;
    private String additionCheck ;
    private String typeCheck = "CUS_CHECK" ;
    private String imei ;
    private String status  ;
    private String videoUrl ;
    private String bankingNumber ;
    private String bankingName ;
    private String bankingUser ;


}
