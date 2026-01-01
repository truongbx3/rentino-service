package com.viettel.vss.dto.attach_file;

import lombok.Data;

import java.io.InputStream;

@Data
public class AttachFileDTO {
    private String fileName;
    private InputStream inputStream;
}
