package com.viettel.vss.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DevicePriceDetailDto {
    Long id;
    private String deviceCode;
    private String type;
    private BigDecimal price;
}
