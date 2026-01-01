package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InforAppDto extends BaseDto {

    private Long serviceRegistrationId;

    private String toolCode;

    private String version;

    private String period;

    private Long limitUser;

    private Date expired;

    private Date startDate;

}
