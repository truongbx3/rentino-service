package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserBorrowDto extends BaseDto{
    private Long id;
    private Long userId;
    private Date borrowFromDate;
    private Date borrowToDate;
    private Long totalDate;
    private BigDecimal fixedFee;
    private String transaction;
    private BigDecimal dailyFee;
    private BigDecimal interestFee;
    private BigDecimal totalFee;
    private BigDecimal valueBorrow;
    private BigDecimal rate;
    private int isDeleted;

}
