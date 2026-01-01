package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

@Data
public class DeviceQuestionAnswerDto extends BaseDto {
    Long id;
    private String questionCode;
    private String answer;
    private String value;
}
