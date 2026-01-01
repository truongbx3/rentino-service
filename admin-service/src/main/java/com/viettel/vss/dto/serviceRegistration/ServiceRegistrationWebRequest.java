package com.viettel.vss.dto.serviceRegistration;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ServiceRegistrationWebRequest {
    private Long ServiceId;

    private MultipartFile image;
}
