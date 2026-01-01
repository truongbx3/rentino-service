package com.viettel.vss.dto.function;

import lombok.Data;

@Data
public class ImportResponseDto {
    private Boolean status;
    private byte[] fileError;
    private String message;
    private int successRecord;
    private int errorRecord;
    private int totalRecord;
}
