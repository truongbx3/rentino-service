package com.viettel.vss.dto.attach_file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author VSS
 * @created 09/08/2022 - 10:13 AM
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAttachFileRequest {
    private String bucketPath;
    private String userName;
    private MultipartFile[] files;
}
