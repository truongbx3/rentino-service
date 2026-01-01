package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class DeviceQuestionDto extends BaseDto {
    Long id;
    private String code;
    private String type;
    private String question;
    private Integer order;
    List<DeviceQuestionAnswerDto> deviceQuestionAnswerDtos;
}
