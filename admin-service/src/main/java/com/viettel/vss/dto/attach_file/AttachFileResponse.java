package com.viettel.vss.dto.attach_file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachFileResponse {
    private Long id;
    private String fileName;
    private String pathFile;
    private Boolean isActive;
    private Long featureId;
    private String feature;
    private String system;
    private String url;
}
