package com.viettel.vss.dto;

import com.viettel.vss.base.BaseDto;
import lombok.Data;


@Data
public class LoginRunnerDto extends BaseDto {

    private String version;

    private String tenanceCode;

    private String apiKey;
}
