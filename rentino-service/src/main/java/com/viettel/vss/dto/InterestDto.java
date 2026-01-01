package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InterestDto extends BaseDto {
    private String type;
    private int isDeleted;
    private BigDecimal price;
    private Date fromDate;
    private Date toDate;
    private int isFixed;
    private String code;
    private String name;
    private BigDecimal maxBorrow;
    private BigDecimal estimateValue;
    private BigDecimal dailyValue;
}
