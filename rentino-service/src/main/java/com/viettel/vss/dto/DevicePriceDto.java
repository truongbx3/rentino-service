package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DevicePriceDto extends BaseDto {
    Long id;
    private String deviceCode;
    private String type;
    private String model;
    private String deviceName;
    private String totalRam;
    private String storage;
    private BigDecimal price;
    private BigDecimal bonus;
}
