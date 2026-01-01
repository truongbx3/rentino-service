package com.viettel.vss.dto.attach_file;

import lombok.Data;

@Data
public class AttachFileResponseDto {
    private Long id;
    private String fileName;
    private String url;
}
