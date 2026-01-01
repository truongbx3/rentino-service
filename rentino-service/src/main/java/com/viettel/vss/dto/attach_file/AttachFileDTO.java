package com.viettel.vss.dto.attach_file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileDTO {
    private String fileName;
    private InputStream inputStream;
}
