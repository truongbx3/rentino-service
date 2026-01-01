package com.viettel.vss.dto;

import lombok.Data;

@Data
public class ImportDetailDto {
    private Boolean status;
    private byte[] fileError;
    private String message;
    private int successRecord;
    private int errorRecord;
    private int totalRecord;
}
