package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BorrowDto extends BaseDto {
    @NotNull
    private BigDecimal rate;
    @NotNull
    private BigDecimal estimateValue;
    @NotNull
    private Date fromDate;
    @NotNull
    private Date toDate;
    @NotNull
    private String transactionid;
}
