package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;
import org.jodconverter.local.office.utils.Lo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BanksDto extends BaseDto {
    private Long  id;
    private String code;
    private String name;
    private int isDeleted;
}
