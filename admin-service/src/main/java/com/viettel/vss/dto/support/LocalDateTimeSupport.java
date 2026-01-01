package com.viettel.vss.dto.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LocalDateTimeSupport {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime start;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime end;
}
